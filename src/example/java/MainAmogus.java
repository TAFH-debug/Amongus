package example.java;
/*
Пасхалка: если тут ничего нет значит я забыл написать про нее
Fixme 24.06.2021
 */

import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import example.java.data.VoteKick;
import example.java.player.Impostor;
import example.java.player.Spectator;
import example.java.game.Game;
import example.java.player.PlayerA;
import example.java.lobby.Lobby;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.mod.Plugin;
import mindustry.net.Administration;

public class MainAmogus extends Plugin {
    int min_players_limit;
    int max_players_limit;


    public static Lobby lob = new Lobby();
    public static Game game = new Game();
    Unit u;

    @Override
    public void init() {
        Events.on(EventType.ServerLoadEvent.class, p -> {
            min_players_limit = 5;
            max_players_limit = 10;
            lob.map();
            Vars.state.rules = lob.setLobbyRules();
            Vars.netServer.openServer();
            Log.info("AMONG US!");
            UnitTypes.dagger.weapons.clear();
        });

        Events.on(EventType.PlayerJoin.class, p -> {
            p.player.sendMessage("[cyan]Welcome!" +
                    "[yellow]This is among us gamemode.");
            if(game.isStarted) {
                p.player.team(Team.derelict);
                p.player.clearUnit();
                Call.infoMessage(p.player.con, "[accent]You are spectator!\n" +
                        "Wait to the end of the game");
                Spectator spectator = new Spectator(p.player);
                Spectator.spectators.add(spectator);
                Log.info("New spectator!");
            }
            else {
                Call.sendMessage("[accent]Wait to other players.Write /info to information about gamemode");
                u = UnitTypes.dagger.spawn(Team.sharded, Team.sharded.core().x(), Team.sharded.core().y + 16);
                p.player.unit(u);
                PlayerA playerA = new PlayerA(p.player);
                PlayerA.players.add(playerA);
                PlayerA.getPlayerA.put(p.player, playerA);
                Call.setHudText("[cyan]Players: " + PlayerA.players.size);
                Log.info("New player!");
            }
        });

        Vars.netServer.admins.addActionFilter(action -> action.type != Administration.ActionType.breakBlock && action.type != Administration.ActionType.configure);
        Vars.netServer.admins.addChatFilter( (player, text) -> {
            if(game.isVoteKick) {
                return text;
            }
            else if(!game.isStarted) {
                return text;
            }
            else {
                return null;
            }
        });

        Events.on(EventType.Trigger.update.getClass(), o -> {
            VoteKick.update();
            Impostor.update();
        });

        Events.on(EventType.TapEvent.class, c -> {
            if(true) {
                return;
            }
            if(!(c.tile.floor() == Blocks.darkPanel1.asFloor())) {
                return;
            }
            if(PlayerA.getPlayerA.get(c.player).isImpostor) {
                Impostor impostor = Impostor.getImpostor.get(c.player);
                impostor.inVent(c.tile);
            }
        });

        Events.on(EventType.TapEvent.class, t -> {
            if(Spectator.getSpectator.containsValue(t.player)) {
                return;
            }
            if(t.tile.block() == Blocks.phaseWall) {
                VoteKick.start();
            }
        });

        Events.on(EventType.PlayerLeave.class, q -> {
            if(PlayerA.getPlayerA.get(q.player) == null) {
                Spectator.spectators.remove(Spectator.getSpectator.get(q.player));
                return;
            }
            PlayerA.players.remove(PlayerA.getPlayerA.get(q.player));
            if(PlayerA.getPlayerA.get(q.player).isImpostor) {
                game.end(false);
            }
        });

        Events.on(EventType.WorldLoadEvent.class, t -> {
            for(int x = 0; x < Vars.world.width(); x++) {
                for(int y = 0; y < Vars.world.height(); y++) {
                    if(Vars.world.tile(x, y).floor() == Blocks.magmarock.asFloor()) {
                        game.spawnX = x * 8;
                        game.spawnY = y * 8;
                        Log.info(x +" " + y +" setted");
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.removeCommand("help");
        handler.<Player>register("info", "info about gamemode", (args,player) -> {
            player.sendMessage("[cyan]Commands:\n " +
                    "[green]/color <id> - choose color by id.\n" +
                    "[scarlet]/sabotage <place> - for impostor sabotage.\n" +
                    "[yellow]/vote <number> - vote to throw a player out into space!\n");
        });

        handler.<Player>register("color", "<id>", "Choose team by id from 1 to 200",  (args,player) -> {
            if(game.isStarted) {
                return;
            }
            try {
                Integer.parseInt(args[0]);
            } catch(NumberFormatException q) {
                player.sendMessage("[scarlet]Color id can be only number!");
                return;
            }
            if(Integer.parseInt(args[0]) == 0) {
                player.sendMessage("[scarlet]You cannot switch team to derelict!Please choose other team.");
                return;
            }
            else if(Integer.parseInt(args[0]) > 200) {
                player.sendMessage("[scarlet]Please select a number less than 200");
                return;
            }
            Team playerTeam = Team.get(Integer.parseInt(args[0]));
            player.team(playerTeam);
        });

        handler.<Player>register("vote","<num..>",  "Vote to throw a player out into space ", (args, player) -> {
            if(game.isVoteKick) {
                int number;
                try {
                    number = Integer.parseInt(args[0]);
                    for(PlayerA playerA : PlayerA.players) {
                        if(playerA.number == number) {
                            playerA.votesToKick++;
                            Call.sendMessage(player.name() + " [accent]has voted to kick " + playerA.player.name());
                            break;
                        }
                    }

                }
                catch (NumberFormatException q) {
                    player.sendMessage("[scarlet]Player ");
                    return;
                }
                PlayerA.players.find(q -> q.number == Integer.parseInt(args[0]));
            }
            else {
                player.sendMessage("[scarlet]NO!");
            }
        });

        handler.<Player>register("sabotage", "<1/2/3>", "Sabotage for impostor.To sabotage write: 1/2/3.Cooldown: 30 sec", (args,player) -> {

        });

        handler.<Player>register("start", "Start game", (args,player) -> {
            game.start();
        });
        handler.<Player>register("restart", "Restart game", (args,player) -> {
            player.sendMessage("Coming soon...");
        });

        handler.<Player>register("status", "Status of the plugin", (args, player) -> {
            player.sendMessage("[tan]--STATUS--\n" +
                    "players: " + PlayerA.players + "\n" +
                    "spectators: " + Spectator.spectators);
        });

        handler.<Player>register("send","<message>", "Send message(only for testing)", (args,player) -> {
            Call.sendMessage(player.name + ": " + args.toString());
        });

    }


}


