## Yu-Gi-Oh Tournament Manager

### Description
The **Yu-Gi-Oh Tournament Manager** is a simple Java-based desktop application designed to help organize and manage Yu-Gi-Oh tournaments. Built using Java Swing for the graphical user interface (GUI), this application allows tournament organizers to easily register players, manage match pairings, track results, and automatically calculate standings across multiple rounds.

This application includes support for Swiss-style pairings with a BYE system for odd-numbered participants, keeping track of wins, losses, and draws for each player throughout the tournament. Final standings are displayed at the end, showing each player's total points and match history.

### Features
- **Player Registration**: Input the number of players and their names.
- **Tournament Setup**: Define the tournament name and the number of rounds.
- **Swiss Pairing System**: Automatically pair players for each round based on their current standings.
- **BYE System**: Assign a BYE (free win) to a player when there’s an odd number of participants.
- **Match Result Tracking**: Input the result of each match (win, loss, or draw) for automatic score calculation.
- **Final Standings**: View the final player standings based on their win-loss-draw record and total points.
- **GUI Interface**: User-friendly interface for managing all tournament interactions.
- **Dynamic Player Stats**: View player stats such as total wins, losses, draws, and points after each round.

### How It Works
1. Enter tournament details: name, number of players, and number of rounds.
2. Register players by entering their names.
3. Start the tournament, where the system will pair players for matches each round based on their standings.
4. Enter match results after each round, and the app will update the player stats accordingly.
5. At the end of the tournament, the final standings will be shown based on accumulated points.

### Technologies Used
- **Java**: Core programming language.
- **Swing**: For the graphical user interface (GUI).

### How to Run
1. Clone this repository: `git clone https://github.com/your-username/YuGiOh-Tournament-Manager.git`
2. Open the project in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse, NetBeans).
3. Compile and run the `YuGiOhTournamentApp.java` class.
4. Follow the instructions in the GUI to manage your Yu-Gi-Oh tournament.

### Future Enhancements
- Save and load tournament data from a file.
- Support for exporting final standings to a CSV or PDF.
- Enhanced UI for better tournament visualization.
- Multi-language support.
