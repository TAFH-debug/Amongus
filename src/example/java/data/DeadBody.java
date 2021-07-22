package example.java.data;

import arc.struct.Seq;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;

public class DeadBody {
    public int Xpos;
    public int Ypos;
    private Unit body;
    public static Seq<DeadBody> deadBodies = new Seq<>();

    public DeadBody(int x, int y) {
        this.Xpos = x;
        this.Ypos = y;
        this.body = UnitTypes.crawler.spawn(Team.derelict, x, y);
        this.body.speedMultiplier(0);
        deadBodies.add(this);
    }
    public void remove() {
        deadBodies.remove(this);
        this.body.damage(1000);
    }
}
