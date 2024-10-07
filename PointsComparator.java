import java.util.Comparator;

public class PointsComparator implements Comparator<Player> {


    @Override
    public int compare(Player player, Player t1) {
        if(player.points == t1.points) return 0;
        if(player.points < t1.points) return -1;
        return 1;
    }
}
