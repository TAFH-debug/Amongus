package example.java.data;

import arc.Core;
import arc.struct.Seq;
import arc.util.Interval;
import example.java.MainAmogus;
import example.java.player.PlayerA;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.world.Tile;

public class Task {
    static Interval interval;
    static Seq<Task> tasks;
    int task_time;
    Tile tile;
    PlayerA player;

    public Task(PlayerA player, Tile tile) {
        this.player = player;
        this.tile = tile;
        player.freeze();
        this.task_time = 0;
        tasks.add(this);
    }
    public void remove() {
        this.task_time = 0;
        tasks.remove(this);
    }
    public void add_time() {
        this.task_time++;
        if(task_time >= 20) {
            remove();
            MainAmogus.game.completedTasks++;
            player.unfreeze();
            tile.setFloor(Blocks.ice.asFloor());
        }
    }

    public static void update() {
        if(interval.get(0, 60f)) {
            if(tasks.size == 0) {
                return;
            }
            for(Task task : tasks) {
                task.add_time();
            }
        }
    }

}
