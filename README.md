# cs2114-project2-group39

# Pokémon Card Collection Tracker

## System diagram

![System diagram: the user types into InputReader, which passes valid input to CollectionApp. CollectionApp hands cards and quantities to CardCollection, which is the only class that touches the two LinkedChains (LinkedChain<PokemonCard> and LinkedChain<EnergyCard>). Results flow back up and are printed.](docs/system-diagram.png)

Input flows down: the user types, `InputReader` validates and re-prompts, `CollectionApp` builds card objects and hands them to `CardCollection`, which is the only class that touches the two LinkedChains. Answers (true/false, counts, the card listing) flow back up the same path and get printed.

## How to compile and run

All files are in package `pokemontracker`, in one `src/pokemontracker` folder. JUnit 5 is needed for the tests (Eclipse/IntelliJ include it, or use `junit-platform-console-standalone.jar`).

**Compile the program:**

```bash
javac -d out src/pokemontracker/*.java
```

(Leave out `*Test.java` if JUnit isn't on the classpath.)

**Run the program:**

```bash
java -cp out pokemontracker.CollectionApp
```

**Compile and run the tests:**

```bash
javac -d out -cp junit-platform-console-standalone.jar src/pokemontracker/*.java
java -jar junit-platform-console-standalone.jar -cp out --scan-classpath
```

In Eclipse: right-click CollectionApp → Run As → Java Application, and right-click a test file → Run As → JUnit Test.

## Reflection (InputReader and CollectionApp)

### What changed from the spec

- Both classes got a second constructor that takes a PrintStream. The spec only had InputReader(Scanner). We added this so the tests can capture what the program prints and check messages like "Your collection is empty."
- Ctrl+D: the spec said run() would check hasMoreInput(). That only works between menu choices. If input ends in the middle of adding a card, the read methods now throw NoSuchElementException, and run() and main() catch it and print "Goodbye!" instead of crashing.
- The spec said CollectionApp would be tested by hand. We wrote JUnit tests for it instead, by running the whole menu from a script of typed lines.
- Validation lives where the spec said: names, numbers, menu choices, rarities, and energy types in InputReader; the empty-collection check in CollectionApp.handleRemove(); "removing more than you own" in CardCollection.

### What happened to the unknowns

- Scanner skipping lines after nextInt(): solved. Every value is read with nextLine() and parsed, so nothing is left behind.
- Testing input code with JUnit: solved by giving InputReader a Scanner built from a string, like new Scanner("abc\n5\n").

### What doesn't work yet

Nothing known in these two classes. The numbered rarity and energy lists depend on the enum order in Rarity and EnergyType (COMMON, UNCOMMON, RARE, HOLO_RARE and GRASS, FIRE, WATER, ...). If that order changes, a few test inputs need updating.

## Reflection (PokemonCard, EnergyCard, Rarity, EnergyType)

### What changed from the spec

- Rarity and EnergyType override toString() to give readable names ("Holo Rare", "Fire"). The spec only listed the constants. We needed this so PokemonCard.toString() prints "Pikachu (Rare)" and EnergyCard.toString() prints "Fire Energy", as the spec's examples show, and so InputReader's numbered lists look clean. (Own reflection.)
- PokemonCard also rejects a null name, not just a blank one. The spec only mentioned blank names, but a null would otherwise crash on trim().
- Fields in both card classes are final. A card's name or type never changes, and this keeps equals/hashCode stable while a card sits in the LinkedChain.
- Everything else matches the spec: equality is by name only, ignoring case and rarity, the name is trimmed in the constructor, and hashCode uses the lowercase name.

### What changed in the LinkedChain

Nothing. We use the provided LinkedChain<T> unchanged. PokemonCard and EnergyCard override equals, so the chain's count, contains, and remove(T) already work for both types.

### What happened to the unknowns

- Making one LinkedChain hold two card types: solved. The same generic class is used as LinkedChain<PokemonCard> and LinkedChain<EnergyCard>. PokemonCardTest.testWorksInLinkedChain and EnergyCardTest.testWorksInLinkedChain prove this.
- Matching "pikachu" and " Pikachu" as the same card: solved. The constructor trims and equals uses equalsIgnoreCase.

### What doesn't work yet

Nothing known in these four classes. The enum order is what InputReader's numbered menus depend on, so testRarityOrder and testEnergyTypeOrder will fail if someone reorders the constants.

## Reflection (LinkedChain and LinkedChainTest) — Ash

### What changed from the spec

- LinkedChain.java was provided as starter code by the instructor, so I kept the original implementation rather than rewriting it.
- I created LinkedChainTest.java to test the functionality of the provided linked chain.
- The test file contains 27 JUnit tests covering all 12 methods in LinkedChain.
- The tests check different operations, including adding elements, removing elements, checking whether elements exist, and handling empty chains.
- I also tested edge cases to make sure the methods behave correctly with invalid inputs and empty data structures.

### What happened to the unknowns

- Testing all the methods in LinkedChain: solved. I created and ran 27 JUnit tests covering all 12 methods.
- Making sure the provided implementation handles edge cases: solved for the cases covered by my tests.
- Understanding how the generic linked chain supports the project: the same LinkedChain implementation can store different types of objects without needing separate implementations.

### What doesn't work yet

I haven't identified any issues with LinkedChain or LinkedChainTest from the tests I ran. All 27 tests passed. Additional integration testing may still be useful as the other project components are completed.

## Reflection (CardCollection and CardCollectionTest) — Golden

### What changed from the spec

- The constructor rejects a null or blank owner name with IllegalArgumentException. The spec didn't say what happens with a bad owner name.
- addPokemon and addEnergy also reject a null card, not just a quantity below 1.
- removePokemon and removeEnergy throw IllegalArgumentException for a blank name, a null type, or a quantity below 1. The spec only described returning false when you don't own enough copies, and that rule is unchanged: the count is checked first, so a failed removal removes nothing.
- countPokemon and countEnergy return 0 for a null or blank input instead of throwing.
- toString prints "<owner>'s collection is empty." for an empty collection. Otherwise it lists Pokémon cards first, then Energy cards, as the spec says.

### What changed in the LinkedChain

Nothing. CardCollection only calls the chain's public methods (add, remove, count, size, isEmpty, toString). It uses two chains, one LinkedChain<PokemonCard> and one LinkedChain<EnergyCard>.

### What doesn't work yet

Nothing known. CardCollectionTest has 11 tests, including an empty collection and removing cards that aren't there, and all of them pass. (Aryan later added 4 more for the trading methods; see below.)

## Stretch goal: Trading between users — Aryan

The trade helper stretch goal from our scope now works. From the menu you can add a new user (7), switch between users (8), and trade cards with another user (9). Options 1–6 are unchanged and always act on the current user's collection.

### How it works

- **Users are stored in a third LinkedChain.** CollectionApp keeps a `LinkedChain<CardCollection>` of every user, so the same generic LinkedChain class now holds three element types: PokemonCard, EnergyCard, and CardCollection. Users are found with `toArray()`, comparing names while ignoring case.
- **TradeOffer** (new) describes what one side gives: `TradeOffer.pokemon("Pikachu", 2)` or `TradeOffer.energy(EnergyType.FIRE, 3)`.
- **TradeHelper.trade(first, firstGives, second, secondGives)** (new) checks that *both* sides own enough before moving anything. If either side is short, it returns false and nothing changes on either side.
- **Rarity is kept.** A new `CardCollection.takePokemon(name, quantity)` removes the cards and returns them, so a traded Holo Rare Pikachu stays Holo Rare for the new owner.

### What changed from the spec

- The spec only named TradeHelper ("finds and performs trades between two CardCollections"). We added TradeOffer so one `trade` method can handle Pokémon-for-Pokémon, Pokémon-for-Energy, and Energy-for-Energy trades instead of four separate methods.
- CardCollection gained `getOwnerName()` (to find and print users) and `takePokemon(...)` (to keep rarity). Its existing methods are unchanged.
- The menu now goes up to 9. Quit stays at 6 so the existing tests and menu numbers didn't change; one test (`testRunBadMenuChoices`) was updated to expect "from 1 to 9".
- The spec's "still unknown" question about splitting a tie between several possible trades didn't come up: the user picks exactly what each side gives.

### Where validation lives (two layers, like the rest of the app)

| Bad input | Caught by | What happens |
| --- | --- | --- |
| Trading with only one user | `CollectionApp.handleTrade` | "You need at least two users to trade." |
| Trading with yourself | `CollectionApp.handleTrade` (backup: `TradeHelper.trade` throws) | "You can't trade with yourself." |
| Trading with / switching to a user who doesn't exist | `CollectionApp` | "No user named Misty." |
| Adding a user name that already exists | `CollectionApp.handleAddUser` | "A user named Sam already exists." |
| Offering more than you own, or a card you don't own | `TradeHelper.trade` | Returns false; app prints "Aryan only has 1 Pikachu. No cards were traded." |
| Blank name, null type, quantity below 1 | `InputReader` re-prompts (backup: `TradeOffer` throws) | Asks again |

### Big-O

`trade` is O((q1 + q2) · n): each side's count is O(n), and moving q cards calls `remove` q times, each O(n). Finding a user is O(u) for u users.

### Tests

TradeOfferTest (8 tests) and TradeHelperTest (8 tests) cover normal trades, rarity being kept, not owning enough, a card not owned, an empty collection, trading with yourself, and null arguments. CardCollectionTest has 4 new tests for `getOwnerName` and `takePokemon`, and CollectionAppTest has 7 new tests that run the add-user, switch-user, and trade menus with good and bad input. All 129 tests pass.

## Next Steps (Fernando)

- Add menu options for the stretch goals once their classes exist: a "Trade" option that calls TradeHelper, and save on quit / load on startup using CollectionFile.
- Startup needs to handle a missing or corrupted save file without crashing.
- New tests: the trade menu with bad input (trading a card you don't own, trading with yourself), and startup with a missing or garbled save file.

## Next Steps (Aryan)

- Trading stretch goal: **done** (see "Stretch goal: Trading between users" above). This also covers the "Trade" menu item in Fernando's next steps.
- Save/load stretch goal: if we do it, add a way to turn a card into a line of text and back (for example PokemonCard.fromLine(String)), which must reject badly formed lines. Saving would also need to store every user, not just one.
- New tests: parsing a valid line, and a bad-input test for a blank or garbled line.

## Next Steps (Ash)

- Continue testing LinkedChain as the other classes are integrated into the project.
- Check that the collection classes correctly use LinkedChain for adding, removing, and counting cards.
- Add integration tests if issues appear when LinkedChain is used with the rest of the application.
- Help identify and fix any issues found during final testing.

## Attribution rows

| File | Team member | Contribution (how) |
| --- | --- | --- |
| InputReader.java | Fernando | Wrote with GenAI assistance; reviewed and tested |
| InputReaderTest.java | Fernando | Wrote with GenAI assistance; ran and verified |
| CollectionApp.java | Fernando; Aryan | Fernando wrote with GenAI assistance; Aryan added users and trading (options 7–9) with GenAI assistance |
| CollectionAppTest.java | Fernando; Aryan | Fernando wrote with GenAI assistance; Aryan added 7 user/trade tests with GenAI assistance |
| Rarity.java | Aryan | Wrote with GenAI assistance; reviewed and tested |
| EnergyType.java | Aryan | Wrote with GenAI assistance; reviewed and tested |
| PokemonCard.java | Aryan | Wrote with GenAI assistance; reviewed and tested |
| EnergyCard.java | Aryan | Wrote with GenAI assistance; reviewed and tested |
| PokemonCardTest.java | Aryan | Wrote with GenAI assistance; ran and verified |
| EnergyCardTest.java | Aryan | Wrote with GenAI assistance; ran and verified |
| LinkedChain.java | Ash | Used instructor-provided starter code without modifying its implementation; reviewed and tested |
| LinkedChainTest.java | Ash | Developed JUnit tests; ran and verified all 27 tests |
| CardCollection.java | Golden; Aryan | Golden wrote; Fernando moved the file into src/pokemontracker; Aryan added getOwnerName and takePokemon with GenAI assistance |
| CardCollectionTest.java | Golden; Aryan | Golden wrote; ran and verified; Aryan added 4 tests for the trading methods with GenAI assistance |
| TradeOffer.java | Aryan | Wrote with GenAI assistance; reviewed and tested |
| TradeOfferTest.java | Aryan | Wrote with GenAI assistance; ran and verified |
| TradeHelper.java | Aryan | Wrote with GenAI assistance; reviewed and tested |
| TradeHelperTest.java | Aryan | Wrote with GenAI assistance; ran and verified |

## GenAI Usage Notes

**GenAI note (Fernando):** I used Claude to draft InputReader, CollectionApp, and their tests from our specification's method signatures and test plan. I reviewed the code, ran the tests, and checked that each bad-input case from our scope is handled where the spec says.

**GenAI note (Aryan):** I used Claude to draft the card classes, enums, and their JUnit tests from our spec's method signatures and test plan. I read each test to check it tests something real, ran them all, and checked that the enum order matches InputReader's numbered menus. For the trading stretch goal, I used Claude to design and draft TradeOffer, TradeHelper, the user and trade menu options, and their tests. I reviewed the code and ran every test.

**GenAI note (Ash):** I used ChatGPT to help with questions about LinkedChain. I kept the instructor-provided LinkedChain implementation unchanged and used the tests to verify its behavior. I ran all 27 JUnit tests and confirmed they passed. I also reviewed the test coverage to make sure all 12 methods were included.
