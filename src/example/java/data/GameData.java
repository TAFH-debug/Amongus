package example.java.data;

import arc.graphics.Color;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.*;

public class GameData {

    static Color color = new Color();
    public static Rules rules = new Rules();


    public static Rules getRules() {
        color.r = 0.005f;
        color.g = 0.0f;
        color.b = 0.02f;
        color.a = 1f;

        rules.enemyLights = false;
        rules.lighting = true;
        rules.fire = false;
        rules.damageExplosions = false;
        rules.reactorExplosions = false;
        rules.ambientLight = color;
        return rules;
    }
}
