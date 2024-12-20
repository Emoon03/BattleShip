=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: ericmoon
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays: 2D arrays allow me to represent the game board for my battleship game.
                The rows and columns of the array will determine the size of the game board.
                I will be able to change the values of the elements in the arrays to represent
                the positioning of the ships. For example, 0 can represent an empty space,
                while 1 signifies that a ship is in that location. I can represent hits and misses
                with -1 and -2 respectively.

  2. Collections: I used TreeMaps and linkedLists to keep track of the positions of the ships on the board as
                  well as their current state in the game (sunk/unsunk). This allows me to decrement the number
                  of ships remaining every time a ship is completely destroyed. This is achieved by mapping
                  each ship to a 2D array of coordinates that represent their location. When all three coordinates
                  are hit, then the ship is added to a linked list of ships to be removed. Once the game state is
                  updated, the ship is removed from the map and number of ships is decremented. This is an
                  appropriate use of the collections concept because it allows for me to keep track of
                  the sunken vs unsunken status of ships which cannot be done by a simple 2D array.

  3. File IO: I used file I/O to save and load the game state. The player will be able to pause the
              game which will store the current state of the board and the player scores into a file.
              Whenever a player wants to load the saved game, my game will read this text file and parse
              the data so it can be displayed. This is an appropriate use of the file IO feature because
              I am effectively incorporating the use of fileWriters and Readers to replicate the information
              of my game state.

  4. Testable Component / JUnit:  I used JUnit testing to test different parts of my game logic.
                                  I did this by creating a separate test class that set up various test methods.
                                  These test cases verified that my game functions worked properly.
                                  For example, I had tests that checked that the board is set up correctly,
                                  checked that player input was correctly accounted for, and checks if the game
                                  properly ends and states the win or loss of the player. I also created functions
                                  that were purely for testing, which set up a model of the BattleShip game without
                                  the element of randomness. This is an appropriate use of the concept because it tests
                                  the encapsulated game model that functions independently of the GUI.

  All of these concepts were approved by my TAs.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  GameBoard - This class instantiates a BattleShip object, which is the model for the game.
               As the user clicks the game board, the model is updated. Whenever the model
               is updated, the game board repaints itself and updates its status JPanel to
               reflect the current state of the model. It also displays the pop-up instructions
               before the game begins.

  RunBattleShip - This class sets up the top-level frame and widgets for the GUI. It determines
                  overall layout of the GUI. It also adds action listeners to
                  allow for the reset, save, and load buttons to work as intended when
                  pressed.

  SinglePlayerBS - This class is a model for BattleShip. Its purpose is to simulate the
                   game of BattleShip for one player. It has methods that generate a 7x7 board,
                   randomize the placement of ships, manage player input, and checks the state of the game.
                   It also allows the player to save and load the current game they are playing. This class
                   contains the major game logic functions that represent how the game is played.

  Game - Main method run to start and run the game. Initializes the runnable game class.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  I had difficulty implementing a way to decrement the number of ships. Every ship in my game is length 3.
  Therefore, the number of ships should decrement only when all three coordinates corresponding to a ship are
  hit by a player. I found this design idea difficult to implement. I had trouble coming up with a way that would
  effectively decrement my ships only when the conditions were right.

  Luckily, I found out I could use collections, more specifically tree maps.
  This would allow me to map each ship to the 3 coordinates they are bound to. In this way, I was able to decrement
  the number of ships remaining in the game by removing the ships from the map only when all coordinates
  the ship is mapped to are guessed by the player.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  I believe there is a good separation of functionality between my 3 classes. I did not run into trouble
  determining which functions go where. The private state is well encapsulated because my SinglePlayerBS class
  instantiates private instance variables that can be accessed in other classes through getter methods.
  If given the chance to refactor, I would perhaps create more subclasses for my SinglePLayerBS class because
  it holds a lot of different functions that are responsible for different parts of the game. This could potentially
  be separated into different classes for better organization.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
