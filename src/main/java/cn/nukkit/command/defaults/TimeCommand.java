package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;

/**
 * Created on 2015/11/11 by xtypr.
 * Package cn.nukkit.command.defaults in project Nukkit .
 */
public class TimeCommand extends VanillaCommand {

    public TimeCommand(String name) {
        super(name, "%nukkit.command.time.description", "%nukkit.command.time.usage");
        this.setPermission("nukkit.command.time.add;nukkit.command.time.set;nukkit.command.time.start;nukkit.command.time.stop");
        this.commandParameters.clear();
        this.commandParameters.put("1arg", new CommandParameter[]{
                new CommandParameter("start|stop", CommandParamType.STRING, false)
        });
        this.commandParameters.put("2args", new CommandParameter[]{
                new CommandParameter("add|set", CommandParamType.STRING, false),
                new CommandParameter("value", CommandParamType.INT, false)
        });
        this.commandParameters.put("2args_", new CommandParameter[]{
                new CommandParameter("add|set", CommandParamType.STRING, false),
                new CommandParameter("value", CommandParamType.STRING, false)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        switch (args[0]) {
            case "start":
                if (!sender.hasPermission("nukkit.command.time.start")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return true;
                }
                for (Level level : sender.getServer().getLevels().values()) {
                    level.checkTime();
                    level.startTime();
                    level.checkTime();
                }
                Command.broadcastCommandMessage(sender, "Restarted the time");
                return true;
            case "stop":
                if (!sender.hasPermission("nukkit.command.time.stop")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return true;
                }
                for (Level level : sender.getServer().getLevels().values()) {
                    level.checkTime();
                    level.stopTime();
                    level.checkTime();
                }
                Command.broadcastCommandMessage(sender, "Stopped the time");
                return true;
            case "query":
                if (!sender.hasPermission("nukkit.command.time.query")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return true;
                }
                Level level;
                if (sender instanceof Player) {
                    level = ((Position) sender).getLevel();
                } else {
                    level = sender.getServer().getDefaultLevel();
                }
                sender.sendMessage(new TranslationContainer("commands.time.query", String.valueOf(level.getTime())));
                return true;
            default:
                break;
        }

        if (args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return false;
        }

        int value;
        switch (args[0]) {
            case "set":
                if (!sender.hasPermission("nukkit.command.time.set")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return true;
                }
                switch (args[1]) {
                    case "day":
                        value = Level.TIME_DAY;
                        break;
                    case "night":
                        value = Level.TIME_NIGHT;
                        break;
                    case "midnight":
                        value = Level.TIME_MIDNIGHT;
                        break;
                    case "noon":
                        value = Level.TIME_NOON;
                        break;
                    case "sunrise":
                        value = Level.TIME_SUNRISE;
                        break;
                    case "sunset":
                        value = Level.TIME_SUNSET;
                        break;
                    default:
                        try {
                            value = Math.max(0, Integer.parseInt(args[1]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                            return true;
                        }
                        break;
                }
                for (Level level : sender.getServer().getLevels().values()) {
                    level.checkTime();
                    level.setTime(value);
                    level.checkTime();
                }
                Command.broadcastCommandMessage(sender, new TranslationContainer("commands.time.set", String.valueOf(value)));
                break;
            case "add":
                if (!sender.hasPermission("nukkit.command.time.add")) {
                    sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
                    return true;
                }
                try {
                    value = Math.max(0, Integer.parseInt(args[1]));
                } catch (NumberFormatException e) {
                    sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                    return true;
                }
                for (Level level : sender.getServer().getLevels().values()) {
                    level.checkTime();
                    level.setTime(level.getTime() + value);
                    level.checkTime();
                }
                Command.broadcastCommandMessage(sender, new TranslationContainer("commands.time.added", String.valueOf(value)));
                break;
            default:
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                break;
        }

        return true;
    }
}
