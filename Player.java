public class Player {
    String name;
    int wins;
    int losses;
    int draws;
    int points;

    Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.points = 0;
    }

    void updateScore(String result) {
        if (result.equals("Win")) {
            wins++;
            points += 3;
        } else if (result.equals("Loss")) {
            losses++;
        } else if (result.equals("Draw")) {
            draws++;
            points++;
        }
    }
}
