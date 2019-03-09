package cn.nukkit.network.protocol;

import cn.nukkit.command.data.CommandData;
import cn.nukkit.command.data.CommandDataVersions;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandOverload;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.BinaryStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AvailableCommandsPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.AVAILABLE_COMMANDS_PACKET;

    private static final ObjIntConsumer<BinaryStream> WRITE_BYTE = (s, v) -> s.putByte((byte) v);
    private static final ObjIntConsumer<BinaryStream> WRITE_SHORT = BinaryStream::putLShort;
    private static final ObjIntConsumer<BinaryStream> WRITE_INT = BinaryStream::putLInt;
    private static final ToIntFunction<BinaryStream> READ_BYTE = BinaryStream::getByte;
    private static final ToIntFunction<BinaryStream> READ_SHORT = BinaryStream::getLShort;
    private static final ToIntFunction<BinaryStream> READ_INT = BinaryStream::getLInt;

    /**
     * This flag is set on all types EXCEPT the POSTFIX type. Not completely sure what this is for, but it is required
     * for the argtype to work correctly. VALID seems as good a name as any.
     */
    public static final int ARG_FLAG_VALID = 0x100000;
    /**
     * Enums are a little different: they are composed as follows:
     * ARG_FLAG_ENUM | ARG_FLAG_VALID | (enum index)
     */
    public static final int ARG_FLAG_ENUM = 0x200000;
    /**
     * This is used for /xp <level: int>L. It can only be applied to integer parameters.
     */
    public static final int ARG_FLAG_POSTFIX = 0x1000000;
    public static final int ARG_FLAG_SOFT_ENUM = 0x4000000;

    /**
     * Basic parameter types. These must be combined with the ARG_FLAG_VALID constant.
     * ARG_FLAG_VALID | (type const)
     */
    public static final int ARG_TYPE_INT = 0x1;
    public static final int ARG_TYPE_FLOAT = 0x2;
    public static final int ARG_TYPE_VALUE = 0x3;
    public static final int ARG_TYPE_WILDCARD_INT = 0x4;
    public static final int ARG_TYPE_OPERATOR = 0x5;
    public static final int ARG_TYPE_TARGET = 0x6;
    public static final int ARG_TYPE_WILDCARD_TARGET = 0x7;

    public static final int ARG_TYPE_FILE_PATH = 0xf;

    public static final int ARG_TYPE_INT_RANGE = 0x13;

    public static final int ARG_TYPE_STRING = 0x1c;

    public static final int ARG_TYPE_POSITION = 0x1e;

    public static final int ARG_TYPE_MESSAGE = 0x21;

    public static final int ARG_TYPE_RAWTEXT = 0x23;

    public static final int ARG_TYPE_JSON = 0x26;

    public static final int ARG_TYPE_COMMAND = 0x2d;

    public Map<String, CommandDataVersions> commands;
    public final Map<String, List<String>> softEnums = new HashMap<>();

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        commands = new HashMap<>();

        List<String> enumValues = new ArrayList<>();
        List<String> postFixes = new ArrayList<>();
        List<CommandEnum> enums = new ArrayList<>();

        int len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            enumValues.add(getString());
        }

        len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            postFixes.add(getString());
        }

        ToIntFunction<BinaryStream> indexReader;
        if (enumValues.size() < 256) {
            indexReader = READ_BYTE;
        } else if (enumValues.size() < 65536) {
            indexReader = READ_SHORT;
        } else {
            indexReader = READ_INT;
        }

        len = (int) getUnsignedVarInt();
        while (len-- > 0) {
            String enumName = getString();
            int enumLength = (int) getUnsignedVarInt();

            List<String> values = new ArrayList<>();

            while (enumLength-- > 0) {
                int index = indexReader.applyAsInt(this);

                String enumValue;

                if (index < 0 || (enumValue = enumValues.get(index)) == null) {
                    throw new IllegalStateException("Enum value not found for index " + index);
                }

                values.add(enumValue);
            }

            enums.add(new CommandEnum(enumName, values));
        }

        len = (int) getUnsignedVarInt();

        while (len-- > 0) {
            String name = getString();
            String description = getString();
            int flags = getByte();
            int permission = getByte();
            CommandEnum alias = null;

            int aliasIndex = getLInt();
            if (aliasIndex >= 0) {
                alias = enums.get(aliasIndex);
            }

            Map<String, CommandOverload> overloads = new HashMap<>();

            int length = (int) getUnsignedVarInt();
            while (length-- > 0) {
                CommandOverload overload = new CommandOverload();

                int paramLen = (int) getUnsignedVarInt();

                overload.input.parameters = new CommandParameter[paramLen];
                for (int i = 0; i < paramLen; i++) {
                    String paramName = getString();
                    int type = getLInt();
                    boolean optional = getBoolean();

                    CommandParameter parameter = new CommandParameter(paramName, optional);


                    if ((type & ARG_FLAG_POSTFIX) != 0) {
                        parameter.postFix = postFixes.get(type & 0xffff);
                    } else if ((type & ARG_FLAG_VALID) == 0) {
                        throw new IllegalStateException("Invalid parameter type received");
                    } else {
                        int index = type & 0xffff;
                        if ((type & ARG_FLAG_ENUM) != 0) {
                            parameter.enumData = enums.get(index);
                        } else if ((type & ARG_FLAG_SOFT_ENUM) != 0) {
                            // TODO: 22/01/2019 soft enums
                        } else {
                            throw new IllegalStateException("Unknown parameter type!");
                        }
                    }

                    overload.input.parameters[i] = parameter;
                }

                overloads.put(Integer.toString(length), overload);
            }

            CommandData data = new CommandData();
            data.aliases = alias;
            data.overloads = overloads;
            data.description = description;
            data.flags = flags;
            data.permission = permission;

            CommandDataVersions versions = new CommandDataVersions();
            versions.versions.add(data);

            this.commands.put(name, versions);
        }
    }

    @Override
    public void encode() {
        this.reset();

        LinkedHashSet<String> enumValuesSet = new LinkedHashSet<>();
        LinkedHashSet<String> postFixesSet = new LinkedHashSet<>();
        LinkedHashSet<CommandEnum> enumsSet = new LinkedHashSet<>();

        commands.forEach((name, data) -> {
            CommandData cmdData = data.versions.get(0);

            if (cmdData.aliases != null) {
                enumsSet.add(cmdData.aliases);

                enumValuesSet.addAll(cmdData.aliases.getValues());
            }

            for (CommandOverload overload : cmdData.overloads.values()) {
                for (CommandParameter parameter : overload.input.parameters) {
                    if (parameter.enumData != null) {
                        enumsSet.add(parameter.enumData);

                        enumValuesSet.addAll(parameter.enumData.getValues());
                    }

                    if (parameter.postFix != null) {
                        postFixesSet.add(parameter.postFix);
                    }
                }
            }
        });

        List<String> enumValues = new ArrayList<>(enumValuesSet);
        List<CommandEnum> enums = new ArrayList<>(enumsSet);
        List<String> postFixes = new ArrayList<>(postFixesSet);

        this.putUnsignedVarInt(enumValues.size());
        enumValues.forEach(this::putString);

        this.putUnsignedVarInt(postFixes.size());
        postFixes.forEach(this::putString);

        ObjIntConsumer<BinaryStream> indexWriter;
        if (enumValues.size() < 256) {
            indexWriter = WRITE_BYTE;
        } else if (enumValues.size() < 65536) {
            indexWriter = WRITE_SHORT;
        } else {
            indexWriter = WRITE_INT;
        }

        this.putUnsignedVarInt(enums.size());
        enums.forEach((cmdEnum) -> {
            putString(cmdEnum.getName());

            List<String> values = cmdEnum.getValues();
            putUnsignedVarInt(values.size());

            for (String val : values) {
                int i = enumValues.indexOf(val);

                if (i < 0) {
                    throw new IllegalStateException("Enum value '" + val + "' not found");
                }

                indexWriter.accept(this, i);
            }
        });

        putUnsignedVarInt(commands.size());

        commands.forEach((name, cmdData) -> {
            CommandData data = cmdData.versions.get(0);

            putString(name);
            putString(data.description);
            putByte((byte) data.flags);
            putByte((byte) data.permission);

            putLInt(data.aliases == null ? -1 : enums.indexOf(data.aliases));

            putUnsignedVarInt(data.overloads.size());
            for (CommandOverload overload : data.overloads.values()) {
                putUnsignedVarInt(overload.input.parameters.length);

                for (CommandParameter parameter : overload.input.parameters) {
                    putString(parameter.name);

                    int type = 0;
                    if (parameter.postFix != null) {
                        int i = postFixes.indexOf(parameter.postFix);
                        if (i < 0) {
                            throw new IllegalStateException("Postfix '" + parameter.postFix + "' isn't in postfix array");
                        }
                        type = ARG_FLAG_POSTFIX | i;
                    } else {
                        type |= ARG_FLAG_VALID;
                        if (parameter.enumData != null) {
                            type |= ARG_FLAG_ENUM | enums.indexOf(parameter.enumData);
                        } else {
                            type |= parameter.type.getId();
                        }
                    }

                    putLInt(type);
                    putBoolean(parameter.optional);
                }
            }
        });

        this.putUnsignedVarInt(softEnums.size());

        softEnums.forEach((name, values) -> {
            this.putString(name);
            this.putUnsignedVarInt(values.size());
            values.forEach(this::putString);
        });
    }
}
