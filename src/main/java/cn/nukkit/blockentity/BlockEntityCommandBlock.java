package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCommandBlock;
import cn.nukkit.block.BlockFaceable;
import cn.nukkit.command.CommandSender;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.permission.PermissibleBase;
import cn.nukkit.permission.Permission;
import cn.nukkit.permission.PermissionAttachment;
import cn.nukkit.permission.PermissionAttachmentInfo;
import cn.nukkit.plugin.Plugin;
import com.google.common.base.Strings;
import java.util.Map;

public class BlockEntityCommandBlock extends BlockEntitySpawnable implements CommandSender, BlockEntityNameable, BlockEntityPowerable {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_REPEATING = 1;
    public static final int MODE_CHAIN = 2; //TODO: this.getLevel().getGameRules().getInt(GameRule.MAX_COMMAND_CHAIN_LENGTH) //连锁执行量上限

    public static final int VERSION = 9;

    public static final String TAG_CONDITIONAL_MODE = "conditionalMode"; //boolean 是否有条件 (默认false)
    public static final String TAG_AUTO = "auto"; //boolean 是否无需红石始终活动 (默认false, 连锁型默认true)
    public static final String TAG_COMMAND = "Command"; //str
    public static final String TAG_LAST_EXECUTION = "LastExecution"; //long 上次触发的游戏刻 (默认0)
    public static final String TAG_TRACK_OUTPUT = "TrackOutput"; //boolean 是否保存上次输出 (默认false)
    public static final String TAG_LAST_OUTPUT = "LastOutput"; //str 上次输出的内容(文本或翻译器) 如 "seargeSays.searge"
    public static final String TAG_LAST_OUTPUT_PARAMS = "LastOutputParams"; //list_str 上次输出的参数(文本或翻译器) 如 "%seargeSays.searge3"
    public static final String TAG_LP_COMMAND_MODE = "LPCommandMode"; //int 上次输出的命令方块模式 (默认2)
    public static final String TAG_LP_CONDIONAL_MODE = "LPCondionalMode"; //boolean 上次输出是否有条件 (默认true)
    public static final String TAG_LP_REDSTONE_MODE = "LPRedstoneMode"; //boolean 上次输出是否需要红石 (默认true)
    public static final String TAG_SUCCESS_COUNT = "SuccessCount"; //int 成功执行的次数 (默认0)
    public static final String TAG_CONDITION_MET = "conditionMet"; //boolean 是否已满足触发条件 (默认false)
    public static final String TAG_VERSION = "Version"; //int 9

    private final PermissibleBase perm;

    private boolean conditionalMode = false;
    private boolean auto = false;
    private String command;
    private long lastExecution = 0;
    private boolean trackOutput = true;
    private String lastOutput;
    private ListTag<StringTag> lastOutputParams = new ListTag<>(TAG_LAST_OUTPUT_PARAMS); //TODO
    private int lastOutputCommandMode = 0;
    private boolean lastOutputCondionalMode = true;
    private boolean lastOutputRedstoneMode = true;
    private int successCount = 0;
    private boolean conditionMet = false;
    private boolean powered = false;

    public BlockEntityCommandBlock(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        this.perm = new PermissibleBase(this);
    }

    @Override
    protected void initBlockEntity() {
        if (this.namedTag.contains(TAG_POWERED)) {
            this.powered = this.namedTag.getBoolean(TAG_POWERED);
        }
        if (this.namedTag.contains(TAG_CONDITIONAL_MODE)) {
            this.conditionalMode = this.namedTag.getBoolean(TAG_CONDITIONAL_MODE);
        }
        if (this.namedTag.contains(TAG_AUTO)) {
            this.auto = this.namedTag.getBoolean(TAG_AUTO);
        }
        if (this.namedTag.contains(TAG_COMMAND)) {
            this.command = this.namedTag.getString(TAG_COMMAND);
        }
        if (this.namedTag.contains(TAG_LAST_EXECUTION)) {
            this.lastExecution = this.namedTag.getLong(TAG_LAST_EXECUTION);
        }
        if (this.namedTag.contains(TAG_TRACK_OUTPUT)) {
            this.trackOutput = this.namedTag.getBoolean(TAG_TRACK_OUTPUT);
        }
        if (this.namedTag.contains(TAG_LAST_OUTPUT)) {
            this.lastOutput = this.namedTag.getString(TAG_LAST_OUTPUT);
        }
        if (this.namedTag.contains(TAG_LAST_OUTPUT_PARAMS)) {
            this.lastOutputParams = (ListTag<StringTag>) this.namedTag.getList(TAG_LAST_OUTPUT_PARAMS);
        }
        if (this.namedTag.contains(TAG_LP_COMMAND_MODE)) {
            this.lastOutputCommandMode = this.namedTag.getInt(TAG_LP_COMMAND_MODE);
        }
        if (this.namedTag.contains(TAG_LP_CONDIONAL_MODE)) {
            this.lastOutputCondionalMode = this.namedTag.getBoolean(TAG_LP_CONDIONAL_MODE);
        }
        if (this.namedTag.contains(TAG_LP_REDSTONE_MODE)) {
            this.lastOutputRedstoneMode = this.namedTag.getBoolean(TAG_LP_REDSTONE_MODE);
        }
        if (this.namedTag.contains(TAG_SUCCESS_COUNT)) {
            this.successCount = this.namedTag.getInt(TAG_SUCCESS_COUNT);
        }
        if (this.namedTag.contains(TAG_CONDITION_MET)) {
            this.conditionMet = this.namedTag.getBoolean(TAG_CONDITION_MET);
        }

        super.initBlockEntity();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.putBoolean(TAG_POWERED, this.powered);
        this.namedTag.putBoolean(TAG_CONDITIONAL_MODE, this.conditionalMode);
        this.namedTag.putBoolean(TAG_AUTO, this.auto);
        if (this.command != null) {
            this.namedTag.putString(TAG_COMMAND, this.command);
        }
        this.namedTag.putLong(TAG_LAST_EXECUTION, this.lastExecution);
        this.namedTag.putBoolean(TAG_TRACK_OUTPUT, this.trackOutput);
        if (this.lastOutput != null) {
            this.namedTag.putString(TAG_LAST_OUTPUT, this.lastOutput);
        }
        if (this.lastOutputParams != null) {
            this.namedTag.putList(this.lastOutputParams);
        }
        this.namedTag.putInt(TAG_LP_COMMAND_MODE, this.lastOutputCommandMode);
        this.namedTag.putBoolean(TAG_LP_CONDIONAL_MODE, this.lastOutputCondionalMode);
        this.namedTag.putBoolean(TAG_LP_REDSTONE_MODE, this.lastOutputRedstoneMode);
        this.namedTag.putInt(TAG_SUCCESS_COUNT, this.successCount);
        this.namedTag.putBoolean(TAG_CONDITION_MET, this.conditionMet);
        this.namedTag.putInt(TAG_VERSION, VERSION);
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag nbt = getDefaultCompound(this, COMMAND_BLOCK)
                .putBoolean(TAG_CONDITIONAL_MODE, this.conditionalMode)
                .putBoolean(TAG_AUTO, this.auto)
                .putLong(TAG_LAST_EXECUTION, this.lastExecution)
                .putBoolean(TAG_TRACK_OUTPUT, this.trackOutput)
                .putInt(TAG_LP_COMMAND_MODE, this.lastOutputCommandMode)
                .putBoolean(TAG_LP_CONDIONAL_MODE, this.lastOutputCondionalMode)
                .putBoolean(TAG_LP_REDSTONE_MODE, this.lastOutputRedstoneMode)
                .putInt(TAG_SUCCESS_COUNT, this.successCount)
                .putBoolean(TAG_CONDITION_MET, this.conditionMet)
                .putInt(TAG_VERSION, VERSION);
        if (this.command != null) {
            nbt.putString(TAG_COMMAND, this.command);
        }
        if (this.lastOutput != null) {
            nbt.putString(TAG_LAST_OUTPUT, this.lastOutput);
        }
        if (this.lastOutputParams != null) {
            nbt.putList(this.lastOutputParams);
        }
        if (this.hasName()) {
            nbt.putString(TAG_CUSTOM_NAME, this.getName());
        }
        return nbt;
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.getBlock() instanceof BlockCommandBlock;
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString(TAG_CUSTOM_NAME) : "!";
    }

    @Override
    public boolean hasName() {
        return this.namedTag.contains(TAG_CUSTOM_NAME);
    }

    @Override
    public void setName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            this.namedTag.remove(TAG_CUSTOM_NAME);
        } else {
            this.namedTag.putString(TAG_CUSTOM_NAME, name);
        }
        this.setDirty();
    }

    @Override
    public void setPowered() {
        this.setPowered(true);
    }

    @Override
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    public boolean isPowered() {
        return this.powered;
    }

    public boolean trigger() {
        if (this.getLastExecution() != this.getServer().getTick()) {
            this.setConditionMet();
            if (this.getLevel().getGameRules().getBoolean(GameRule.COMMAND_BLOCKS_ENABLED) && this.isConditionMet()) {
                if (this.getCommand().equalsIgnoreCase("Searge")) {
                    this.lastOutput = "#itzlipofutzli";
                    this.successCount = 1;
                } else {
                    this.lastOutput = null;
                    String cmd = this.getCommand();
                    if (cmd.startsWith("/")) {
                        cmd = cmd.substring(1);
                    }
                    if (Server.getInstance().dispatchCommand(this, cmd)) {
                        this.successCount = 1; //TODO: >1
                    } else {
                        this.successCount = 0;
                    }
                }
                this.lastExecution = this.getServer().getTick();
                this.lastOutputCommandMode = this.getMode();
                this.lastOutputCondionalMode = this.isConditional();
                this.lastOutputRedstoneMode = !this.isAuto();
            } else {
                this.successCount = 0;
            }
            this.setDirty();
            return true;
        }
        return false;
    }

    public int getMode() {
        Block block = this.getBlock();
        if (block.getId() == Block.REPEATING_COMMAND_BLOCK) {
            return MODE_REPEATING;
        } else if (block.getId() == Block.CHAIN_COMMAND_BLOCK) {
            return MODE_CHAIN;
        }
        return MODE_NORMAL;
    }

    /**
     * Returns the command of the command block.
     *
     * @return command
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Sets the command.
     *
     * @param command
     */
    public void setCommand(String command) {
        this.command = command;
        this.successCount = 0;
        this.setDirty();
    }

    public boolean isAuto() {
        return this.auto;
    }

    public void setAuto(boolean auto) {
        //boolean autoed = this.auto;
        this.auto = auto;
        //if (!autoed && this.auto && !this.powered && this.getMode() != MODE_CHAIN) {
        //    this.setConditionMet();
        //}
        this.setDirty();
    }

    public boolean isConditional() {
        return this.conditionalMode;
    }

    public void setConditional(boolean conditionalMode) {
        this.conditionalMode = conditionalMode;
        this.setConditionMet();
        //this.setDirty();
    }

    public boolean isConditionMet() {
        return this.conditionMet;
    }

    public boolean setConditionMet() {
        if (this.isConditional() && this.getBlock() instanceof BlockCommandBlock) {
            Block block = this.getBlock().getSide(((BlockFaceable) this.getBlock()).getBlockFace().getOpposite());
            if (block instanceof BlockCommandBlock) {
                BlockEntityCommandBlock commandBlock = ((BlockCommandBlock) block).getBlockEntity();
                this.conditionMet = commandBlock.getSuccessCount() > 0;
            } else {
                this.conditionMet = false;
            }
        } else {
            this.conditionMet = true;
        }
        this.setDirty();
        return this.conditionMet;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    public void setSuccessCount(int count) {
        this.successCount = count;
        this.setDirty();
    }

    public long getLastExecution() {
        return this.lastExecution;
    }

    public void setLastExecution(long time) {
        this.lastExecution = time;
        this.setDirty();
    }

    public boolean isTrackingOutput() {
        return this.trackOutput;
    }

    public void setTrackOutput(boolean track) {
        this.trackOutput = track;
        this.setDirty();
    }

    public String getLastOutput() {
        return this.lastOutput;
    }

    public void setLastOutput(String output) {
        if (Strings.isNullOrEmpty(output)) {
            this.lastOutput = null;
        } else {
            this.lastOutput = output;
        }
        this.setDirty();
    }

    public int getLastOutputCommandMode() {
        return this.lastOutputCommandMode;
    }

    public void setLastOutputCommandMode(int mode) {
        this.lastOutputCommandMode = mode;
        this.setDirty();
    }

    public boolean getLastOutputCondionalMode() {
        return this.lastOutputCondionalMode;
    }

    public void setLastOutputCondionalMode(boolean condionalMode) {
        this.lastOutputCondionalMode = condionalMode;
        this.setDirty();
    }

    public boolean getLastOutputRedstoneMode() {
        return this.lastOutputRedstoneMode;
    }

    public void setLastOutputRedstoneMode(boolean redstoneMode) {
        this.lastOutputRedstoneMode = redstoneMode;
        this.setDirty();
    }

    public void setLastOutputParams(ListTag<StringTag> params) {
        this.lastOutputParams = params;
        this.setDirty();
    }

    @Override
    public boolean isPermissionSet(String name) {
        return this.perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return this.perm.isPermissionSet(permission);
    }

    @Override
    public boolean hasPermission(String name) {
        return this.perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.perm.hasPermission(permission);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return this.perm.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name) {
        return this.perm.addAttachment(plugin, name);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, Boolean value) {
        return this.perm.addAttachment(plugin, name, value);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        this.perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        this.perm.recalculatePermissions();
    }

    @Override
    public Map<String, PermissionAttachmentInfo> getEffectivePermissions() {
        return this.perm.getEffectivePermissions();
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    @Override
    public boolean isCommandBlock() {
        return true;
    }

    @Override
    public Server getServer() {
        return Server.getInstance();
    }

    @Override
    public void sendMessage(TextContainer message) {
        this.sendMessage(this.getServer().getLanguage().translate(message));
    }

    @Override
    public void sendMessage(String message) {
        message = this.getServer().getLanguage().translateString(message);
        if (this.isTrackingOutput()) {
            this.lastOutput = message;
        }
        if (this.getLevel().getGameRules().getBoolean(GameRule.COMMAND_BLOCK_OUTPUT)) {
            for (Player player : this.getLevel().getPlayers().values()) {
                if (player.isOp()) {
                    player.sendMessage(message);
                }
            }
        }
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {

    }
}
