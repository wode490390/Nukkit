package cn.nukkit.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.MathHelper;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.CommandException;
import cn.nukkit.utils.Identifier;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandSelector {

    public static Vector3 getCoordinate(String rawX, String rawY, String rawZ, CommandSender sender) {
        double x = 0;
        double y = 0;
        double z = 0;
        if (sender instanceof Vector3) {
            Vector3 pos = (Vector3) sender;
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
        }
        x = parseCoordinate(rawX, x);
        y = parseCoordinate(rawY, y);
        z = parseCoordinate(rawZ, z);
        return new Vector3(x, y, z);
    }

    public static double parseCoordinate(String raw){
        return parseCoordinate(raw, 0);
    }

    public static double parseCoordinate(String raw, double base){
        boolean relative = false;
        if (raw.equals("~")) {
            return base;
        } else if (raw.startsWith("~")) {
            raw = raw.substring(1);
            relative = true;
        }
        double coord;
        try {
            coord = Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return base;
        }
        return relative ? base + coord : coord;
    }

    @Deprecated
    public static Player atP(Position pos) {
        Player target = null;
        double distance = Double.MAX_VALUE;
        if (pos != null) {
            for (Entity entity : pos.getLevel().getEntities()) {
                if (entity instanceof Player) {
                    double test = entity.distance(pos);
                    if (test < distance) {
                        distance = test;
                        target = (Player) entity;
                    }
                }
            }
        }
        return target;
    }

    @Deprecated
    public static Player[] atA() {
        Set<Player> targets = new HashSet<>();
        for (Level level : Server.getInstance().getLevels().values()) {
            for (Entity entity : level.getEntities()) {
                if (entity instanceof Player) {
                    targets.add((Player) entity);
                }
            }
        }
        return targets.toArray(new Player[0]);
    }

    @Deprecated
    public static Player atR() {
        Player[] targets = atA();
        if (targets.length > 0) {
            return targets[ThreadLocalRandom.current().nextInt(targets.length)];
        }
        return null;
    }

    @Deprecated
    public static Entity[] atE() {
        Set<Entity> targets = new HashSet<>();
        for (Level level : Server.getInstance().getLevels().values()) {
            targets.addAll(Arrays.asList(level.getEntities()));
        }
        return targets.toArray(new Entity[0]);
    }

    @Deprecated
    public static CommandSender atS(CommandSender sender) {
        return sender;
    }

    /**
     * This matches the at-tokens introduced for command blocks, including their
     * arguments, if any.
     */
    private static final Pattern ENTITY_SELECTOR = Pattern.compile("^@([pares])(?:\\[([^ ]*)\\])?$");
    private static final Splitter ARGUMENT_SEPARATOR = Splitter.on(',').omitEmptyStrings();
    private static final Splitter ARGUMENT_JOINER = Splitter.on('=').limit(2);

    private static final String ARG_X = registerArgument("x");
    private static final String ARG_Y = registerArgument("y");
    private static final String ARG_Z = registerArgument("z");
    private static final String ARG_DX = registerArgument("dx");
    private static final String ARG_DY = registerArgument("dy");
    private static final String ARG_DZ = registerArgument("dz");
    private static final String ARG_R = registerArgument("r");
    private static final String ARG_RM = registerArgument("rm");
    private static final String ARG_C = registerArgument("c");
    private static final String ARG_L = registerArgument("l");
    private static final String ARG_LM = registerArgument("lm");
    private static final String ARG_M = registerArgument("m");
    private static final String ARG_NAME = registerArgument("name");
    private static final String ARG_RX = registerArgument("rx");
    private static final String ARG_RXM = registerArgument("rxm");
    private static final String ARG_RY = registerArgument("ry");
    private static final String ARG_RYM = registerArgument("rym");
    private static final String ARG_TYPE = registerArgument("type");
    //private static final String ARG_SCORES = registerArgument("scores"); //TODO:scoreboard
    //private static final String ARG_TAG = registerArgument("tag"); //TODO:scoreboard
    //private static final String ARG_TEAM = registerArgument("team"); //TODO:scoreboard

    private static final Set<String> ARGS = Sets.<String>newHashSet();
    private static final Set<String> WORLD_ARGS = Sets.newHashSet(ARG_X, ARG_Y, ARG_Z, ARG_DX, ARG_DY, ARG_DZ, ARG_RM, ARG_R);

    private static final Predicate<String> VALID_ARGUMENT = (String arg) -> arg != null && (ARGS.contains(arg));

    private static String registerArgument(String arg) {
        ARGS.add(arg);
        return arg;
    }

    /**
     * Returns the one player that matches the given at-token.Returns null if more than one player matches.
     * @param sender CommandSender
     * @param token String
     * @return player Entity
     */
    public static Entity matchOnePlayer(CommandSender sender, String token) throws CommandException {
        return matchOneEntity(sender, token);
    }

    public static List<Entity> matchPlayers(CommandSender sender, String token) throws CommandException {
        return matchEntities(sender, token);
    }

    public static Entity matchOneEntity(CommandSender sender, String token) throws CommandException {
        List<Entity> list = matchEntities(sender, token);
        return list.size() == 1 ? list.get(0) : null;
    }

    public static List<Entity> matchEntities(CommandSender sender, String token) throws CommandException {
        Matcher matcher = ENTITY_SELECTOR.matcher(token);
        if (matcher.matches() && sender instanceof Player) {
            Map<String, String> map = getArgumentMap(matcher.group(2));
            if (isEntityTypeValid(sender, map)) {
                Player player = (Player) sender;
                String s = matcher.group(1);
                BlockVector3 blockpos = getBlockPosFromArguments(map, player.asBlockVector3());
                Vector3 vec3d = getPosFromArguments(map, player);
                List<Level> list = getWorlds(sender, map);
                List<Entity> list1 = Lists.<Entity>newArrayList();

                for (Level world : list) {
                    if (world != null) {
                        List<Predicate<Entity>> list2 = Lists.<Predicate<Entity>>newArrayList();
                        list2.addAll(getTypePredicates(map, s));
                        list2.addAll(getXpLevelPredicates(map));
                        list2.addAll(getGamemodePredicates(map));
                        //list2.addAll(getTeamPredicates(map));
                        //list2.addAll(getScorePredicates(sender, map));
                        list2.addAll(getNamePredicates(map));
                        //list2.addAll(getTagPredicates(map));
                        list2.addAll(getRadiusPredicates(map, vec3d));
                        list2.addAll(getRotationsPredicates(map));

                        if ("s".equalsIgnoreCase(s)) {
                            Player entity = player;

                            if (entity != null) {
                                if (map.containsKey(ARG_DX) || map.containsKey(ARG_DY) || map.containsKey(ARG_DZ)) {
                                    int i = getInt(map, ARG_DX, 0);
                                    int j = getInt(map, ARG_DY, 0);
                                    int k = getInt(map, ARG_DZ, 0);
                                    AxisAlignedBB axisalignedbb = getAABB(blockpos, i, j, k);

                                    if (!axisalignedbb.intersectsWith(entity.getBoundingBox())) {
                                        return Collections.<Entity>emptyList();
                                    }
                                }

                                for (Predicate<Entity> predicate : list2) {
                                    if (!predicate.apply(entity)) {
                                        return Collections.<Entity>emptyList();
                                    }
                                }
                                return Lists.newArrayList(entity);
                            }
                            return Collections.<Entity>emptyList();
                        }
                        list1.addAll(filterResults(map, list2, s, world, blockpos));
                    }
                }
                return getEntitiesFromPredicates(list1, map, sender, s, vec3d);
            }
        }
        return Collections.<Entity>emptyList();
    }

    private static List<Level> getWorlds(CommandSender sender, Map<String, String> argumentMap) {
        List<Level> list = Lists.<Level>newArrayList();
        if (hasWorldArgument(argumentMap) && sender instanceof Player) {
            list.add(((Position) sender).getLevel());
        } else {
            list.addAll(sender.getServer().getLevels().values());
        }
        return list;
    }

    private static <T extends Entity> boolean isEntityTypeValid(CommandSender commandSender, Map<String, String> params) {
        String type = getArgument(params, ARG_TYPE);
        if (type != null) {
            String identifier = type.startsWith("!") ? type.substring(1) : type;
            if (Identifier.getEntityIdentifier().containsKey(identifier)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private static List<Predicate<Entity>> getTypePredicates(Map<String, String> params, String type) {
        String arg = getArgument(params, ARG_TYPE);
        if (arg != null && (type.equals("e") || type.equals("r")|| type.equals("s"))) {
            boolean inverted = arg.startsWith("!");
            return Collections.singletonList(new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    return (Identifier.toEntityId(inverted ? arg.substring(1) : arg, false) == entity.getNetworkId()) != inverted;
                }
            });
        } else {
            return !type.equals("e") && !type.equals("s") ? Collections.singletonList(new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    return entity instanceof Player;
                }
            }) : Collections.emptyList();
        }
    }

    private static List<Predicate<Entity>> getXpLevelPredicates(Map<String, String> params) {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        int i = getInt(params, ARG_LM, -1);
        int j = getInt(params, ARG_L, -1);

        if (i > -1 || j > -1) {
            list.add((Entity entity) -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    return (i <= -1 || player.getExperienceLevel() >= i) && (j <= -1 || player.getExperienceLevel() <= j);
                }
                return false;
            });
        }

        return list;
    }

    private static List<Predicate<Entity>> getGamemodePredicates(Map<String, String> params) {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        String gm = getArgument(params, ARG_M);

        if (gm != null) {
            boolean inverted = gm.startsWith("!");
            if (inverted) {
                gm = gm.substring(1);
            }
            int gamemode;
            try {
                gamemode = Integer.parseInt(gm);
            } catch (NumberFormatException e) {
                gamemode = parseGameMode(gm, -1);
            }

            final int f = gamemode;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        int mode = player.getGamemode();
                        return inverted ? mode != f : mode == f;
                    }
                    return false;
                }
            });
        }
        return list;
    }

    private enum GameMode {
        SURVIVAL(0, "survival", "s"),
        CREATIVE(1, "creative", "c"),
        ADVENTURE(2, "adventure", "a"),
        SPECTATOR(3, "spectator", "sp");

        int id;
        String name;
        String shortName;

        GameMode(int id, String name, String shortName) {
            this.id = id;
            this.name = name;
            this.shortName = shortName;
        }
    }

    public static int parseGameMode(String name, int fallback) {
        for (GameMode gamemode : GameMode.values()) {
            if (gamemode.name.equals(name) || gamemode.shortName.equals(name)) {
                return gamemode.id;
            }
        }
        return fallback;
    }

    private static List<Predicate<Entity>> getNamePredicates(Map<String, String> params) {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();
        String name = getArgument(params, ARG_NAME);

        if (name != null) {
            boolean inverted = name.startsWith("!");
            if (inverted) {
                name = name.substring(1);
            }
            String f = name;
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    return entity != null && entity.getName().equals(f) != inverted;
                }
            });
        }
        return list;
    }

    private static List<Predicate<Entity>> getRadiusPredicates(Map<String, String> params, Vector3 pos) {
        double rm = getInt(params, ARG_RM, -1);
        double r = getInt(params, ARG_R, -1);
        boolean rm_inverted = rm < -0.5d;
        boolean r_inverted = r < -0.5d;

        if (rm_inverted && r_inverted) {
            return Collections.<Predicate<Entity>>emptyList();
        } else {
            double rm_square = Math.pow(Math.max(rm, 1.0E-4d), 2);
            double r_square = Math.pow(Math.max(r, 1.0E-4d), 2);
            return Lists.newArrayList(new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    if (entity != null) {
                        double squaredDistance = pos.distanceSquared(entity);
                        return (rm_inverted || squaredDistance >= rm_square) && (r_inverted || squaredDistance <= r_square);
                    }
                    return false;
                }
            });
        }
    }

    private static List<Predicate<Entity>> getRotationsPredicates(Map<String, String> params) {
        List<Predicate<Entity>> list = Lists.<Predicate<Entity>>newArrayList();

        if (params.containsKey(ARG_RYM) || params.containsKey(ARG_RY)) {
            int rym = clampAngle(getInt(params, ARG_RYM, 0));
            int ry = clampAngle(getInt(params, ARG_RY, 359));
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    if (entity != null) {
                        int i1 = clampAngle(MathHelper.floor(entity.getYaw()));

                        if (rym > ry) {
                            return i1 >= rym || i1 <= ry;
                        } else {
                            return i1 >= rym && i1 <= ry;
                        }
                    }
                    return false;
                }
            });
        }

        if (params.containsKey(ARG_RXM) || params.containsKey(ARG_RX)) {
            int rxm = clampAngle(getInt(params, ARG_RXM, 0));
            int rx = clampAngle(getInt(params, ARG_RX, 359));
            list.add(new Predicate<Entity>() {
                @Override
                public boolean apply(Entity entity) {
                    if (entity == null) {
                        return false;
                    } else {
                        int pitch = clampAngle(MathHelper.floor(entity.getPitch()));
                        if (rxm > rx) {
                            return pitch >= rxm || pitch <= rx;
                        } else {
                            return pitch >= rxm && pitch <= rx;
                        }
                    }
                }
            });
        }

        return list;
    }

    private static int clampAngle(int angle) {
        angle = angle % 360;

        if (angle >= 180) {
            angle -= 360;
        }

        if (angle < -180) {
            angle += 360;
        }

        return angle;
    }

    private static List<Entity> filterResults(Map<String, String> params, List<Predicate<Entity>> inputList, String type, Level level, BlockVector3 position) {
        List<Entity> list = Lists.<Entity>newArrayList();
        String s = getArgument(params, ARG_TYPE);
        s = s != null && s.startsWith("!") ? s.substring(1) : s;
        boolean flag = !type.equals("e");
        boolean flag1 = type.equals("r") && s != null;
        int i = getInt(params, ARG_DX, 0);
        int j = getInt(params, ARG_DY, 0);
        int k = getInt(params, ARG_DZ, 0);
        int l = getInt(params, ARG_R, -1);
        Predicate<Entity> predicate = Predicates.and(inputList);

        if (!params.containsKey(ARG_DX) && !params.containsKey(ARG_DY) && !params.containsKey(ARG_DZ)) {
            if (l >= 0) {
                AxisAlignedBB axisalignedbb1 = new SimpleAxisAlignedBB(position.getX() - l, position.getY() - l, position.getZ() - l, position.getX() + l + 1, position.getY() + l + 1, position.getZ() + l + 1);

                if (flag && !flag1) {
                    list.addAll(getPlayers(level, predicate));
                } else {
                    list.addAll(getNearbyEntities(level, axisalignedbb1, predicate));
                }
            } else if (type.equals("a")) {
                list.addAll(getPlayers(level, predicate));
            } else if (!type.equals("p") && (!type.equals("r") || flag1)) {
                list.addAll(getEntities(level, predicate));
            } else {
                list.addAll(getPlayers(level, predicate));
            }
        } else {
            AxisAlignedBB axisalignedbb = getAABB(position, i, j, k);

            if (flag && !flag1) {
                Predicate<Entity> predicate2 = new Predicate<Entity>() {
                    @Override
                    public boolean apply(Entity p_apply_1_) {
                        return p_apply_1_ != null && axisalignedbb.intersectsWith(p_apply_1_.getBoundingBox());
                    }
                };
                list.addAll(getPlayers(level, Predicates.and(predicate2, predicate)));
            } else {
                list.addAll(getNearbyEntities(level, axisalignedbb, predicate));
            }
        }

        return list;
    }

    private static List<Entity> getEntitiesFromPredicates(List<Entity> matchingEntities, Map<String, String> params, CommandSender sender, String type, final Vector3 pos) {
        int i = getInt(params, ARG_C, !type.equals("a") && !type.equals("e") ? 1 : 0);

        if (!type.equals("p") && !type.equals("a") && !type.equals("e")) {
            if (type.equals("r")) {
                Collections.shuffle(matchingEntities);
            }
        } else {
            Collections.sort(matchingEntities, new Comparator<Entity>() {
                @Override
                public int compare(Entity entity1, Entity entity2) {
                    return ComparisonChain.start().compare(entity1.distanceSquared(pos), entity2.distanceSquared(pos)).result();
                }
            });
        }

        Entity entity = null;
        if (sender instanceof Entity) {
            entity = (Entity) sender;
        }

        if (entity != null && i == 1 && matchingEntities.contains(entity) && !"r".equals(type)) {
            matchingEntities = Lists.newArrayList(entity);
        }

        if (i != 0) {
            if (i < 0) {
                Collections.reverse(matchingEntities);
            }

            matchingEntities = matchingEntities.subList(0, Math.min(Math.abs(i), matchingEntities.size()));
        }

        return matchingEntities;
    }

    private static AxisAlignedBB getAABB(BlockVector3 pos, int x, int y, int z) {
        boolean flag = x < 0;
        boolean flag1 = y < 0;
        boolean flag2 = z < 0;
        int x1 = pos.getX() + (flag ? x : 0);
        int y1 = pos.getY() + (flag1 ? y : 0);
        int z1 = pos.getZ() + (flag2 ? z : 0);
        int x2 = pos.getX() + (flag ? 0 : x) + 1;
        int y2 = pos.getY() + (flag1 ? 0 : y) + 1;
        int z2 = pos.getZ() + (flag2 ? 0 : z) + 1;
        return new SimpleAxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    private static BlockVector3 getBlockPosFromArguments(Map<String, String> params, BlockVector3 pos) {
        return new BlockVector3(getInt(params, ARG_X, pos.getX()), getInt(params, ARG_Y, pos.getY()), getInt(params, ARG_Z, pos.getZ()));
    }

    private static Vector3 getPosFromArguments(Map<String, String> params, Vector3 pos) {
        return new Vector3(getCoordinate(params, ARG_X, pos.getX(), true), getCoordinate(params, ARG_Y, pos.getY(), false), getCoordinate(params, ARG_Z, pos.getZ(), true));
    }

    private static double getCoordinate(Map<String, String> params, String key, double defaultD, boolean offset) {
        return params.containsKey(key) ? getInt(params.get(key), MathHelper.floor(defaultD)) + (offset ? 0.5D : 0.0D) : defaultD;
    }

    private static boolean hasWorldArgument(Map<String, String> params) {
        for (String s : WORLD_ARGS) {
            if (params.containsKey(s)) {
                return true;
            }
        }
        return false;
    }

    private static int getInt(Map<String, String> params, String key, int defaultI) {
        return params.containsKey(key) ? getInt(params.get(key), defaultI) : defaultI;
    }

    private static int getInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static String getArgument(Map<String, String> params, String key) {
        return params.get(key);
    }

    /**
     * Returns whether the given pattern can match more than one target.
     * @param selectorStr String
     * @return boolean
     */
    public static boolean matchesMultiplePlayers(String selectorStr) throws CommandException {
        Matcher matcher = ENTITY_SELECTOR.matcher(selectorStr);
        if (matcher.matches()) {
            Map<String, String> map = getArgumentMap(matcher.group(2));
            String s = matcher.group(1);
            return getInt(map, ARG_C, !"a".equals(s) && !"e".equals(s) ? 1 : 0) != 1;
        }
        return false;
    }

    /**
     * Returns whether the given token has any arguments set.
     * @param selectorStr String
     * @return boolean
     */
    public static boolean hasArguments(String selectorStr) {
        return ENTITY_SELECTOR.matcher(selectorStr).matches();
    }

    private static Map<String, String> getArgumentMap(String argumentString) throws CommandException {
        Map<String, String> map = Maps.<String, String>newHashMap();
        if (argumentString != null) {
            for (String s : ARGUMENT_SEPARATOR.split(argumentString)) {
                Iterator<String> iterator = ARGUMENT_JOINER.split(s).iterator();
                String s1 = iterator.next();

                if (!VALID_ARGUMENT.apply(s1)) {
                    throw new CommandException("Unknown command argument: " + s);
                }

                map.put(s1, iterator.hasNext() ? iterator.next() : "");
            }
        }
        return map;
    }

    private static List<Entity> getEntities(Level level, Predicate<Entity> filter) {
        List<Entity> list = Lists.<Entity>newArrayList();
        for (Entity entity : level.getEntities()) {
            if (filter.apply(entity)) {
                list.add(entity);
            }
        }
        return list;
    }

    private static List<Player> getPlayers(Level level, Predicate<Entity> filter) {
        List<Player> list = Lists.<Player>newArrayList();
        for (Player player : level.getPlayers().values()) {
            if (filter.apply(player)) {
                list.add(player);
            }
        }
        return list;
    }

    private static List<Entity> getNearbyEntities(Level level, AxisAlignedBB aabb, Predicate<Entity> filter) {
        List<Entity> list = Lists.<Entity>newArrayList();
        for (Entity entity : level.getNearbyEntities(aabb)) {
            if (filter.apply(entity)) {
                list.add(entity);
            }
        }
        return list;
    }
}
