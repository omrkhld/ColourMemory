Hi,

This is my implementation of the Colour Memory game as per the instructions of the assignment. The Github repository is located here: https://github.com/omrkhld/ColourMemory
Also, I have copied the apk file from the default output folder to the main folder for your convenience.

The following is the folder structure I used to organize my files, along with a small description of their purpose:
colourmemory
	activities //this folder stores all the Activities
		HighscoresActivity //the Activity for the High Scores page
		MainActivity //the Activity that is shown when the app is launched
		PlayActivity //the Activity that shows the game board
	adapters //this folder stores the Adapters used by RecyclerViews
		CardAdapter //the Adapter used to display the cards in a 4x4 grid, also applying various UI/UX related information such as onClickListeners and Animations
		HighscoresRealmAdapter //the Adapter used to display the high scores from the Realm database
	fragments //this folder stores all the Fragments
		HighscoresDialog //a DialogFragment that shows the high scores. Accessible from PlayActivity
	models //this folder stores the models/POJOs
		Card //the model for each Card
		Highscore //the model for each high score. Persisted by Realm.
	utils //this folder is for miscellaneous utilities
		FlipAnimation //provides each Card in CardAdapter with a flip animation when clicked
	MainApplication //the Main Application file used to get the instance of the Realm database

Additionally, I have used the following external libraries to assist me:
Realm - Local mobile database, for storing and retrieving high scores
Butterknife - For injecting views in Activities, Fragments, and Adapters. Removes boilerplate code.
EventBus - Establish communication between Activities, Fragments, and Adapters. Used to signal to the Activity that a change has occurred in the Adapter.

The development process of this application was very interesting as it allowed me to experiment with certain implementation methods and compare the differences between them. Initially, I used a GridView to display the cards in a 4x4 grid. After completing that, I decided to try it out with a RecyclerView and a GridLayoutManager, because the extreme customizability of the RecyclerView through its Adapter was appealing.

I faced some challenges, especially when dealing with animating the cards, as I had not had much experience with implementing animations in apps before. Ultimately, I managed to achieve the intended result. Another challenge I faced was trying to determine what did or did not belong in the Adapter. I know that the Adapter is meant to determine the UI, so I did not want to bog it down with too much logic. As such, I used an EventBus to send the relevant information to the Activity to execute the game logic which subsequently changes the data (through notifyDataSetChanged()) used by the Adapter to display the game board.

I decided to use the Realm database as I had quite a lot of experience in utilising it during the development of other apps. Its simplicity, speed, and small size was too good to pass up. Furthermore, Realm provided its very own RealmAdapter when working with RecyclerViews.

Overall, this was a good experience and allowed me to flex my coding skills. I look forward to hearing your thoughts on it.

Regards,
Omar