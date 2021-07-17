package example.java.lobby;

import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;

import arc.graphics.Color;

public class Lobby  {

    private Rules lobby = new Rules();
    private Color color = new Color(0.005f, 0.0f, 0.02f, 0.8f);

    public Rules setLobbyRules() {
        lobby.lighting = false;
        lobby.enemyLights = true;
        lobby.damageExplosions = false;
        lobby.reactorExplosions = false;
        lobby.fire = false;
        lobby.ambientLight = color;
        return lobby;
    }

    public void map() {
        Seq<Player> players = new Seq<>();
        Groups.player.copy(players);

        Vars.logic.reset();

        Vars.world.loadMap(Vars.maps.byName("Lobby"));

        Vars.state.rules = setLobbyRules().copy();
        Call.worldDataBegin();
        Log.info("Go!");

        Vars.logic.play();

        for (Player player : players) {
            player.team(Team.sharded);
            Vars.netServer.sendWorldData(player);
        }
    }
}