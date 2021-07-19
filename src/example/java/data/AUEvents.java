package example.java.data;

import mindustry.gen.Player;

public class AUEvents {

    public static class DeadBodyFoundEvent {
        public final Player founder;
        public final DeadBody deadBody;
        public DeadBodyFoundEvent(Player founder, DeadBody deadBody) {
            this.founder = founder;
            this.deadBody = deadBody;
        }
    }
    public class VotekickEvent {
        public VotekickEvent() {
        }
    }
    public class SabotageEvent {

    }
}
