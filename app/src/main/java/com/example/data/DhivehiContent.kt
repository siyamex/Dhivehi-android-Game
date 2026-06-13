package com.example.data

// Models for Content items
data class ThaanaLetter(
    val letter: String,         // e.g. "ހ" (Haa)
    val nameEnglish: String,     // e.g. "Haa"
    val wordDhivehi: String,     // e.g. "ހަނޑު"
    val wordEnglish: String,     // e.g. "Moon"
    val phonicsDhivehi: String,  // e.g. "ހަ"
    val description: String,     // e.g. "Haa is the first letter representing H."
    val exampleSentenceDhivehi: String = "",
    val exampleSentenceEnglish: String = ""
)

data class DhivehiNumber(
    val numericValue: Int,
    val numeralDhivehi: String,  // e.g. "އެކެއް"
    val numeralEnglish: String,  // e.g. "Ekeh (One)"
    val detailsDhivehi: String,  // e.g. "1 މަސް" (1 fish)
    val detailsEnglish: String   // e.g. "1 Fish"
)

data class DhivehiColor(
    val nameDhivehi: String,     // e.g. "ރަތް"
    val nameEnglish: String,     // e.g. "Red"
    val colorHex: String,        // e.g. "#FF0000"
    val exampleDhivehi: String,  // e.g. "ރަތް ދޮންކެޔޮ" (Red apple)
    val exampleEnglish: String   // e.g. "Red banana"
)

data class DhivehiShape(
    val nameDhivehi: String,     // e.g. "ވަށްބުރު"
    val nameEnglish: String,     // e.g. "Circle"
    val description: String,
    val iconName: String         // e.g. "circle"
)

data class VocabItem(
    val dhivehi: String,
    val translation: String,
    val category: String, // "Animals", "Fruits", "Fruits & Veggies", "Sea Creatures", "Maldives"
    val phonics: String,
    val sentenceDhivehi: String,
    val sentenceEnglish: String
)

data class GrammarLesson(
    val id: String,
    val title: String,
    val explanationDhivehi: String,
    val explanationEnglish: String,
    val examples: List<Pair<String, String>>,
    val quizQuestion: String,
    val quizOptions: List<String>,
    val quizAnswerIndex: Int
)

data class StaticStory(
    val id: String,
    val title: String,
    val desc: String,
    val pages: List<String>,            // Dhivehi pages
    val pagesEnglish: List<String>,    // English translation pages
    val comprehensionQuestion: String,
    val comprehensionOptions: List<String>,
    val comprehensionAnswerIndex: Int
)

data class DhivehiSong(
    val title: String,
    val lyricsDhivehi: String,
    val lyricsEnglish: String,
    val rhythmDescription: String
)

object DhivehiContent {

    // 24 main letters of the Maldivian script (Thaana)
    val alphabet = listOf(
        ThaanaLetter("ހ", "Haa", "ހަނދު", "Moon", "Haa", "The first letter of Thaana. It makes the 'H' sound like in Handhu (Moon).", "ހަނދު ވަރަށް ރީތި.", "The moon is very beautiful."),
        ThaanaLetter("ށ", "Shaviyani", "ށަވިޔަނި", "S", "Sha", "The second letter. It makes the 'Sh' sound.", "ށަވިޔަނި އަކުރުން ފެށޭ ބަސްތައް ގިނައެވެ.", "There are many words starting with Shaviyani."),
        ThaanaLetter("ނ", "Noonu", "ނާށި", "Coconut Shell", "Noo", "Makes the 'N' sound like in Naashi (Coconut shell).", "ނާށިގަނޑުން ތަށި ހަދައެވެ.", "Cups are made from coconut shells."),
        ThaanaLetter("ރ", "Raa", "ރިޔާ", "Sail", "Raa", "Makes the 'R' sound like in Riyaa (Sail of a Dhoni boat).", "ރިޔާ ދޯނި ކަނޑުގައި ދުވެއެވެ.", "The sailboat sails in the sea."),
        ThaanaLetter("ބ", "Baa", "ބޮލި", "Cowry Shell", "Baa", "Makes the 'B' sound like in Boli (Cowry Shell - historic currency).", "ބޮލި އަކީ އިހުގެ ފައިސާއެވެ.", "Cowry shells were the currency of old times."),
        ThaanaLetter("ޅ", "Lhaviyani", "ޅަފަތް", "Young sprout", "Lha", "Makes a retroflex 'Lh' sound as in Lhafath (Young).", "ޅަ ކުދިން ކިޔަވަން ޖެހެއެވެ.", "Young kids need to study."),
        ThaanaLetter("ކ", "Kaafu", "ކަނޑު", "Ocean", "Kaa", "Makes the 'K' sound like in Kandu (Ocean/Sea).", "ކަނޑުގައި ވަރަށް ގިނަ މަސް އުޅެއެވެ.", "There are many fish living in the sea."),
        ThaanaLetter("އ", "Alifu", "އިރު", "Sun", "Aa", "Acts as a vowel carrier or makes a glottal stop / 'A' sound as in Iru (Sun).", "އިރު އަރަނީ އިރުމަތީންނެވެ.", "The sun rises from the east."),
        ThaanaLetter("ވ", "Vaavu", "ވައި", "Wind", "Vaa", "Makes the 'V' or 'W' sound like in Vai (Wind/Air).", "ވައި ވަރަށް ގަދަޔަށް އެބަޖެހެއެވެ.", "The wind is blowing very strongly."),
        ThaanaLetter("މ", "Meemu", "މަސް", "Fish", "Mee", "Makes the 'M' sound like in Mas (Fish - iconic livelihood of Maldives).", "މިއީ ވަރަށް މީރު ކަނޑުމަހެކެވެ.", "This is a very delicious ocean fish."),
        ThaanaLetter("ފ", "Faafu", "ފަރު", "Coral Reef", "Faa", "Makes the 'F' sound like in Faru (Coral reef).", "ފަރުތަކުގައި ރީތި ކުލަކުލަ ހުރެއެވެ.", "The coral reefs have beautiful colors."),
        ThaanaLetter("ދ", "Dhaalu", "ދޯނި", "Traditional Boat", "Dhaa", "Makes the 'Dh' sound like in Dhoni (Maldivian traditional boat).", "ދޯނިން ކަނޑަށް ފުރަނީއެވެ.", "The traditional boat is departing to sea."),
        ThaanaLetter("ތ", "Thaa", "ތަރި", "Star", "Thaa", "Makes a dental 'Th' sound like in Thari (Star).", "އުޑުމަތީގައި ގިނަ ތަރިތައް ފެންނަން ހުރެއެވެ.", "Many stars can be seen in the sky."),
        ThaanaLetter("ލ", "Laamu", "ލުނބޯ", "Lime", "Laa", "Makes the 'L' sound like in Lunbo (Lime).", "ލުނބޯ ހުންނަނީ ހުތްކޮށެވެ.", "Limes are very sour."),
        ThaanaLetter("ގ", "Gaafu", "ގަސް", "Tree", "Gaa", "Makes the 'G' sound like in Gas (Tree/Palm tree).", "ރުއްގަސްތަކުން ހިޔާ ލިބެއެވެ.", "Trees and palms give us shade."),
        ThaanaLetter("ޏ", "gnaviyani", "ޏޭލަ", "Seagull", "Gna", "Makes a nasal palatal 'Gn' sound like in Gnaela.", "ޏޭލަ އަކީ ރީތި ދޫންޏެކެވެ.", "The seagull is a beautiful bird."),
        ThaanaLetter("ސ", "Seenu", "ސައި", "Tea", "See", "Makes the 'S' sound like in Sai (Tea).", "ހެނދުނު ހޫނު ސައި ބޮއެވެ.", "We drink warm tea in the morning."),
        ThaanaLetter("ޑ", "Daviyani", "ޑިންގީ", "Dinghy Boat", "Da", "Makes the rolled retroflex 'D' sound as in Dinghy boat.", "ޑިންގީ ބޭނުންކޮށްގެން ދަތުރުކުރެއެވެ.", "We travel using the dinghy boat."),
        ThaanaLetter("ޒ", "Zavu", "ޒުވާރި", "Maize / Corn", "Zaa", "Makes the 'Z' sound like in Zuvaari (Corn/Maize).", "ޒުވާރި ކާން ވަރަށް މީރެވެ.", "Corn is delicious to eat."),
        ThaanaLetter("ޓ", "Taviyani", "ޓޮމާޓޯ", "Tomato", "Taa", "Makes the retroflex 'T' sound like in Tomato.", "ޓޮމާޓޯ ހުންނަނީ ރަތްކޮށެވެ.", "Tomatoes are red in color."),
        ThaanaLetter("ޔ", "Yaafu", "ޔޫސުފްގެބަސް", "Yusuf", "Yaa", "Makes the 'Y' sound.", "ޔާފު އިން ފެށޭ ބަސް މަދެވެ.", "There are few words starting with Yaafu."),
        ThaanaLetter("ޕ", "Paviyani", "ޕާން", "Bread", "Paa", "Makes the 'P' sound like in Paan (Bread).", "ހެނދުނު ސަޔާއި ޕާން ކައެވެ.", "We eat bread with morning tea."),
        ThaanaLetter("ޖ", "Jeemu", "ޖަގު", "Jug", "Jaa", "Makes the 'J' sound like in Jagu (Jug).", "ޖަގުގައި ފިނި ފެން އެބަހުއްޓެވެ.", "There is cold water in the jug."),
        ThaanaLetter("ޗ", "Chaviyani", "ޗަމްޕާ", "Frangipani Flower", "Cha", "Makes the 'Ch' sound like in Champa (Maldivian flower).", "ޗަމްޕާ މަލުގެ ވަސް ވަރަށް މީރެވެ.", "The frangipani flower smells very sweet.")
    )

    // Numbers 1 to 10
    val numbers = listOf(
        DhivehiNumber(1, "އެކެއް", "Ekeh (1)", "އެއް މަސްގަނޑު", "1 Fish"),
        DhivehiNumber(2, "ދޭއް", "Dheh (2)", "ދެ ކަޅުއޮށް", "2 Sailboats"),
        DhivehiNumber(3, "ތިނެއް", "Thineh (3)", "ތިން ރުއްގަސް", "3 Palm Trees"),
        DhivehiNumber(4, "ހަތަރެއް", "Hathareh (4)", "ހަތަރު ފަރުމަސް", "4 Reef Fish"),
        DhivehiNumber(5, "ފަހެއް", "Faheh (5)", "ފަސް ބޮލިގަނޑު", "5 Cowry Shells"),
        DhivehiNumber(6, "ހައެއް", "Haaeh (6)", "ހަ ދޫނި", "6 Sea Birds"),
        DhivehiNumber(7, "ހަތެއް", "Hatheh (7)", "ހަތް ދޯނި", "7 Dhoni Boats"),
        DhivehiNumber(8, "އަށެއް", "Asheh (8)", "އަށް ތަރި", "8 Stars"),
        DhivehiNumber(9, "ނުވައެއް", "Nuvaeh (9)", "ނުވަ ކޮކާލު", "9 Butterflies"),
        DhivehiNumber(10, "ދިހައެއް", "Dihaeh (10)", "ދިހަ ލުނބޯ", "10 Limes")
    )

    // Beach Colors
    val colors = listOf(
        DhivehiColor("ނޫ", "Blue", "#0077B6", "ނޫ ކަނޑު", "Blue Ocean"),
        DhivehiColor("ރަތް", "Red", "#E63946", "ރަތް ބޮލި", "Red Shell"),
        DhivehiColor("ފެހި", "Green", "#2A9D8F", "ފެހި ރުއް", "Green Palm"),
        DhivehiColor("ރީނދޫ", "Yellow", "#E9C46A", "ރީނދޫ އިރު", "Yellow Sun"),
        DhivehiColor("ހުދު", "White", "#FFFFFF", "ހުދު ދޮންވެލި", "White Sand"),
        DhivehiColor("ކަޅު", "Black", "#121B2A", "ކަޅު މަސް", "Black Fish"),
        DhivehiColor("އޮރެންޖު", "Orange", "#F4A261", "އޮރެންޖު އިރުއޮއްސުން", "Orange Sunset"),
        DhivehiColor("ދަނބު", "Purple", "#8338EC", "ދަނބު މާގަނޑު", "Purple Flower")
    )

    // Shapes
    val shapes = listOf(
        DhivehiShape("ވަށްބުރު", "Circle", "Round like the tropical sun (Iru) or a yummy green lime (Lunbo).", "circle"),
        DhivehiShape("އަކުރު", "Square", "Has four equal sides like a textbook or cardboard game block.", "square"),
        DhivehiShape("ތިންކަން", "Triangle", "Three corners like the sail (Riyaa) of our fishing Dhoni boat.", "triangle"),
        DhivehiShape("ދިގުއަކުރު", "Rectangle", "Broad and long like your screen or a school notebook.", "rectangle"),
        DhivehiShape("ތަރި", "Star", "Five points shining high up in the blue Maldives night sky.", "star")
    )

    // Kid Vocabulary Catalog
    val vocabulary = listOf(
        // Animals
        VocabItem("ބުޅާ", "Cat", "Animals", "Bulaa", "ބުޅާ މިއާއުލާ އެބަގޮވައެވެ.", "The cat is meowing."),
        VocabItem("ކުއްތާ", "Dog", "Animals", "Kuttaa", "ކުއްތާ ވަރަށް ހަލުވިކޮށް ދުވެއެވެ.", "The dog runs very quickly."),
        VocabItem("ދޫނި", "Bird", "Animals", "Dhooni", "ދޫނި ރުކެއްގެ މަތީގައި އިނދެ ގޮވަނީއެވެ.", "The bird sits on top of the coconut tree chirping."),
        VocabItem("ކޮކާލު", "Butterfly", "Animals", "Kokaalu", "ކޮކާލު މަލަކަށް ޖައްސަނީއެވެ.", "The butterfly sits on a flower."),
        // Fruits
        VocabItem("ކެޔޮ", "Banana", "Fruits", "Keyo", "ކެޔޮ ކެއުމަކީ ހަށިގަނޑަށް ފައިދާހުރި ކަމެކެވެ.", "Eating bananas is healthy for your body."),
        VocabItem("ލުނބޯ", "Lime", "Fruits", "Lunbo", "ލުނބޯ ޖޫސް ވަރަށް ބޯން މީރެވެ.", "Lime juice is delicious to drink."),
        VocabItem("އާފަލު", "Apple", "Fruits", "Aafalu", "ތިމަންނާ ކޮންމެ ދުވަހަކު އާފަލެއް ކަމެވެ.", "I eat an apple every day."),
        VocabItem("މެންގޯ", "Mango", "Fruits", "Mango", "މިއީ ވަރަށް ފޮނި އަނބެކެވެ.", "This is a very sweet mango."),
        // Sea Creatures (Island special!)
        VocabItem("ވެލާ", "Turtle", "Sea Creatures", "Velaa", "ވެލާ ދޮންވެލިގަނޑު މަތީގައި ބިސްއަޅައެވެ.", "The turtle lays eggs on the soft white sand."),
        VocabItem("ކަޅުބިލަމަސް", "Tuna", "Sea Creatures", "Kalhubila Mas", "ކަޅުބިލަމަހަކީ ރާއްޖޭގެ މަޝްހޫރު މަހެކެވެ.", "Skipjack tuna is a very famous fish in the Maldives."),
        VocabItem("ކަނޑުމަސް", "Ocean Fish", "Sea Creatures", "Kandumas", "ކަނޑުމަސް ކެއުމަކީ ކުދިން ބާރުގަދަވާ ކަމެކެވެ.", "Eating ocean fish makes children strong."),
        VocabItem("ކަނޑުއަދިރި", "Dolphin", "Sea Creatures", "Koamas", "ކޯމަސްތައް ކަނޑުމަތީގައި ފުމެ އުފާކުރެއެވެ.", "Dolphins jump in the ocean and celebrate.")
    )

    // Grammar Lessons
    val grammarLessons = listOf(
        GrammarLesson(
            "nouns",
            "Nouns (އިސްމު)",
            "Nouns are words that name people, places, animals, or things. In Dhivehi, this is called 'Ismu' (އިސްމު).",
            "Nouns are the words you see around you! For example, 'Gas' (tree) or 'Velaa' (turtle).",
            listOf(
                "މަސް (Mas) - Fish" to "މަސް ކަނޑުގައި އުޅެއެވެ. (Fish lives in sea.)",
                "ރުއް (Ruh) - Palm tree" to "ރުއް ވަރަށް ދިގެވެ. (The palm tree is very tall.)"
            ),
            "Identify the Noun (އިސްމު) in the sentence: 'ވެލާ ދޮންވެލިގަނޑު މަތީގައި އުޅެއެވެ.'",
            listOf("ވެލާ (Turtle)", "ދުވެއެވެ (Runs)", "ރީތި (Beautiful)"),
            0
        ),
        GrammarLesson(
            "verbs",
            "Verbs (ފިއުލު)",
            "Verbs are action words! They tell us what someone or something is doing. In Dhivehi, are known as 'Fiulu' (ފިއުލު).",
            "Verbs express actions. For example, 'Kiance' (reads), 'Duve' (runs), or 'Kae' (eats).",
            listOf(
                "ދުވުން (Duve) - Running" to "ބުޅާ ވަރަށް ބާރަށް ދުވެއެވެ. (The cat runs very fast.)",
                "ލިޔުން (Liye) - Writing" to "ކޮއްކޮ ފިލާވަޅު ލިޔެއެވެ. (Little sister/brother writes homework.)"
            ),
            "Which of these words is an active Verb (ފިއުލު) meaning 'To Eat'?",
            listOf("ގަސް (Tree)", "ކެއުން (Eat)", "ކަނޑު (Sea)"),
            1
        ),
        GrammarLesson(
            "adjectives",
            "Adjectives (ނަންއިތުރު)",
            "Adjectives describe nouns! They tell us how the nouns look, taste, feel, or sound.",
            "In Dhivehi, we use words like 'Reethi' (beautiful) or 'Kuda' (small) to add description.",
            listOf(
                "ރީތި (Reethi) - Beautiful" to "މިއީ ވަރަށް ރީތި މަލެކެވެ. (This is a very beautiful flower.)",
                "ބާރު (Baaru) - Strong / Fast" to "ވައި ވަރަށް ބާރަށް ޖެހެއެވެ. (The wind is blowing very strongly.)"
            ),
            "Find the Adjective describing the boat: 'މިއީ ބޮޑު ދޯންޏެކެވެ.' (This is a big boat.)",
            listOf("ދޯނި (boat)", "ބޮޑު (Big)", "މިއީ (This is)"),
            1
        )
    )

    // Maldives traditional stories with complete pages
    val stories = listOf(
        StaticStory(
            "turtle_friend",
            "Velaa and the Shining Star",
            "Follow the friendly turtle Velaa as he swims to find a glowing pearl with help from local reef fish.",
            listOf(
                "އެއްދުވަހަކު، ރާއްޖޭގެ ކަނޑުގެ ފަޅެއްގައި އުޅޭ ވެލާ ކަނޑަށް ފީނިއެވެ.",
                "ފަރުތަކުގެ ތެރެއިން ވެލާއަށް ފެނުނީ ވަރަށް ރީތި، ރަންކުލައިގެ ބޮއްޔެކެވެ.",
                "ބޮލީގެ ތެރެއިން ފެނުނީ ވަރަށް އަލިގަދަ މުތެކެވެ.",
                "ވެލާ ބުނެފިއެވެ. 'މިއީ އަހަރެންގެ ރީތި ރަށަށް ލިބުނު ވަރަށް އަގުހުރި ހަދިޔާއެކެވެ!'"
            ),
            listOf(
                "One day, Velaa the turtle who lived in a beautiful Maldivian lagoon dived deep into the ocean.",
                "Among the coral reefs, Velaa found a stunning gold-colored cowry shell.",
                "Inside the shell shone a very bright, glowing ocean pearl.",
                "Velaa smiled and said, 'This is a very precious gift for my beautiful island!'"
            ),
            "What did Velaa find inside the cowry shell?",
            listOf("މަހެއް (A fish)", "މުތެއް (A pearl)", "ކޮކާލެއް (A butterfly)"),
            1
        ),
        StaticStory(
            "crab_clover",
            "The Clever Crab Kakuni",
            "Kakuni the beach crab teaches children how to keep our beautiful sandy channels clean.",
            listOf(
                "ރަށުގެ ދޮންވެލިގަނޑު މަތީގައި ކުޑަ ކަކުނި ހިނގަމުން ދިޔައެވެ.",
                "ދޮންވެލިގަނޑުގައި ހުރީ މީހުން އުކާލާފައި ހުރި ޕްލާސްޓިކް ކުނި ކޮޅެކެވެ.",
                "ކަކުނި އޭގެ ކޮނޑުގައި ހިފައި، ގޮނޑުދޮށުން ކުނިތައް ނަގައި ސާފުކުރިއެވެ.",
                "އެންމެން ބުނެފިއެވެ. 'އަހަރެމެން ރަށުގެ ގޮނޑުދޮށް ސާފުކޮށް ބާއްވަން ޖެހެއެވެ!'"
            ),
            listOf(
                "On the soft white sands of the beach, a little crab was walking along the shore.",
                "On the sand lay some discarded plastic bottles thrown away by visitors.",
                "The crab carried them in its claws, cleaning up the lovely sandy coast.",
                "Everyone agreed: 'We must always keep our island beaches clean and plastic-free!'"
            ),
            "Who cleaned the plastic bottle from the sandy beach?",
            listOf("ކަކުނި (The crab)", "ދޫނި (The bird)", "ކޯމަސް (The dolphin)"),
            0
        )
    )

    // Interactive Songs
    val songs = listOf(
        DhivehiSong(
            "Thaana Alphabet Song",
            "ހ ށ ނ ރ ބ ޅ ކަށެވެ.\nއަހަރެމެންގެ ތަފާތު ބަހަށެވެ.\nއަ އަ އާ އާ އަމަލު ކުރަމާ.\nހުރިހާ ކުދިން އެކުގައި ކިޔަމާ!",
            "Haa, Shaviyani, Noonu, Raa, Baa, Lhaviyani, Kaafu.\nWe sing for our distinct language!\nAa, Aa, Aa, let's learn happily.\nAll children sing together!",
            "Melodic and catchy, sung along to the rhythm of Boduberu drums (Maldives drums)."
        ),
        DhivehiSong(
            "Beautiful Blue Lagoon",
            "ނޫ ނޫ ކަނޑު ފަޅު އެވެ.\nރާއްޖޭގެ ރީތި ދޮންވެލި ތޮށި އެވެ.\nމަސް މަސް ކުލަ ކުލަ އެވެ.\nއުފަލާއެކު ފަތާލަމާ ހިންގާށެވެ!",
            "Blue, blue beautiful ocean lagoon.\nLovely soft white sandbar of the Maldives.\nFish of so many colorful stripes.\nCome, let's swim happily in the warm water!",
            "Calming wave-like lullaby matching soft ocean ripples."
        )
    )
}
