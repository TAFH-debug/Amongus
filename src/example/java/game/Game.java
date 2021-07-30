package example.java.game;

import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Log;
import example.java.MainAmogus;
import example.java.data.DeadBody;
import example.java.data.GameData;
import example.java.data.Position;
import example.java.data.Task;
import example.java.player.Impostor;
import example.java.player.PlayerA;
import example.java.player.Spectator;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.net.Packets;


public class Game {
    public int playersLimit;
    public int impostors;
    public boolean isStarted;
    public boolean isVoteKick;
    public boolean isSabotage;
    public int skipVotes;
    public Seq<Position> spawnPos;
    public int completedTasks = 0;

    public void Game() {
        playersLimit = 5;
        impostors = 1;
        completedTasks = 0;
        isStarted = false;
        isVoteKick = false;
        isSabotage = false;
    }

    //Начало игры
    public void start() {
        Call.sendMessage("[green]Requied number of players collected.Starting a game...");
        int i = 0;
        while(i != Groups.player.size()) {
            PlayerA playerA = new PlayerA(Groups.player.index(i));
            PlayerA.players.add(playerA);
            PlayerA.getPlayerA.put(Groups.player.index(i), playerA);
            i++;
        }
        loadMap();
        Vars.state.rules = GameData.getRules();
        Impostor.chooseImpostor();
        Call.hideHudText();
        for(PlayerA player : PlayerA.players) {
            if(player.player.team() == Team.sharded) {
                player.player.team(Team.get(Mathf.random(2, 250)));
            }
            PlayerA.spawn();
        }
        isStarted = true;
    }

    public void end(Boolean isImpostorWin) {
        if(isImpostorWin) {
            for(Player pl : Groups.player) {
                if(Impostor.getImpostor.containsValue(pl)) {
                    Call.infoMessage(pl.con, "[cyan]VICTORY!");
                    return;
                }
                Call.infoMessage(pl.con, "[scarlet]GAME OVER![] \n" +
                        Impostor.player.name + " [accent]is Impostor!\n" +
                        "[green]Retrying to the lobby...");
            }
        }
        else {
            for(Player pl : Groups.player) {
                if(Impostor.getImpostor.containsValue(pl)) {
                    Call.infoMessage(pl.con, "[scarlet]Game over!");
                    return;
                }
                Call.infoMessage(pl.con, "[cyan]VICTORY![] \n" +
                        Impostor.player.name + " [accent]is Impostor!\n" +
                        "[green]Retrying to the lobby...");
            }
        }
        MainAmogus.lob.map();
        restart();
    }

    public void loadMap() {
        Seq<Player> players = new Seq<>();
        Groups.player.copy(players);

        Vars.logic.reset();

        Vars.world.loadMap(Vars.maps.byName("Amongus"));
        Vars.logic.play();

        for (Player player : players) {
            Vars.netServer.sendWorldData(player);
        }
        Log.info("Go!");
    }
    public void restart() {
        for(DeadBody body : DeadBody.deadBodies) {
            body.remove();
        }
        Log.info("---SERVER RESTARTING---");
        Log.info("Restarting!");
        for(Player player : Groups.player) {
            player.kick("Server restarting!");
        }
        PlayerA.getPlayerA.clear();
        PlayerA.players.clear();
        PlayerA.getPlayerAbyNum.clear();
        Impostor.getImpostor.clear();
        Spectator.spectators.clear();
        Spectator.getSpectator.clear();
    }
    public void update() {
        for(PlayerA playerA : PlayerA.players) {
            if(Vars.world.tile(playerA.player.tileX(), playerA.player.tileY()).floor() == Blocks.grass.asFloor()) {
                Task task = new Task(playerA, Vars.world.tile(playerA.player.tileX(), playerA.player.tileY()));
            }
        }
    }
}