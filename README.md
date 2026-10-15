# cs2114-project2-group39
Pokémon Card Collection Tracker

## How to compile and run

All files are in package `pokemontracker`, in one `src/pokemontracker` folder.
JUnit 5 is needed for the tests (Eclipse/IntelliJ include it, or use
`junit-platform-console-standalone.jar`).

```
# compile the program
javac -d out src/pokemontracker/*.java   # (leave out *Test.java if JUnit isn't on the classpath)

# run the program
java -cp out pokemontracker.CollectionApp

# compile and run the tests
javac -d out -cp junit-platform-console-standalone.jar src/pokemontracker/*.java
java -jar junit-platform-console-standalone.jar -cp out --scan-classpath
```

In Eclipse: right-click `CollectionApp` → Run As → Java Application, and
right-click a test file → Run As → JUnit Test.

## Reflection (InputReader and CollectionApp)

**What changed from the spec**
- Both classes got a second constructor that takes a `PrintStream`. The spec
  only had `InputReader(Scanner)`. We added this so the tests can capture
  what the program prints and check messages like "Your collection is empty."
- Ctrl+D: the spec said `run()` would check `hasMoreInput()`. That only works
  between menu choices. If input ends in the middle of adding a card, the
  read methods now throw `NoSuchElementException`, and `run()` and `main()`
  catch it and print "Goodbye!" instead of crashing.
- The spec said `CollectionApp` would be tested by hand. We wrote JUnit tests
  for it instead, by running the whole menu from a script of typed lines.
- Validation lives where the spec said: names, numbers, menu choices,
  rarities, and energy types in `InputReader`; the empty-collection check in
  `CollectionApp.handleRemove()`; "removing more than you own" in
  `CardCollection`.

**What happened to the unknowns**
- Scanner skipping lines after `nextInt()`: solved. Every value is read with
  `nextLine()` and parsed, so nothing is left behind.
- Testing input code with JUnit: solved by giving `InputReader` a `Scanner`
  built from a string, like `new Scanner("abc\n5\n")`.

**What doesn't work yet**
- Nothing known in these two classes. The numbered rarity and energy lists
  depend on the enum order in `Rarity` and `EnergyType` (COMMON, UNCOMMON,
  RARE, HOLO_RARE and GRASS, FIRE, WATER, ...). If that order changes, a few
  test inputs need updating.

## Reflection (PokemonCard, EnergyCard, Rarity, EnergyType)

**What changed from the spec**
- `Rarity` and `EnergyType` override `toString()` to give readable names
  ("Holo Rare", "Fire"). The spec only listed the constants. We needed this
  so `PokemonCard.toString()` prints "Pikachu (Rare)" and
  `EnergyCard.toString()` prints "Fire Energy", as the spec's examples show,
  and so InputReader's numbered lists look clean. (Own reflection.)
- `PokemonCard` also rejects a `null` name, not just a blank one. The spec
  only mentioned blank names, but a null would otherwise crash on `trim()`.
- Fields in both card classes are `final`. A card's name or type never
  changes, and this keeps `equals`/`hashCode` stable while a card sits in the
  LinkedChain.
- Everything else matches the spec: equality is by name only, ignoring case
  and rarity, the name is trimmed in the constructor, and `hashCode` uses the
  lowercase name.

**What changed in the LinkedChain**
- Nothing. We use the provided `LinkedChain<T>` unchanged. `PokemonCard` and
  `EnergyCard` override `equals`, so the chain's `count`, `contains`, and
  `remove(T)` already work for both types.

**What happened to the unknowns**
- *Making one LinkedChain hold two card types*: solved. The same generic
  class is used as `LinkedChain<PokemonCard>` and `LinkedChain<EnergyCard>`.
  `PokemonCardTest.testWorksInLinkedChain` and
  `EnergyCardTest.testWorksInLinkedChain` prove this.
- *Matching "pikachu" and " Pikachu" as the same card*: solved. The
  constructor trims and `equals` uses `equalsIgnoreCase`.

**What doesn't work yet**
- Nothing known in these four classes. The enum order is what InputReader's
  numbered menus depend on, so `testRarityOrder` and `testEnergyTypeOrder`
  will fail if someone reorders the constants.

## Next Steps (Fernando)
- Add menu options for the stretch goals once their classes exist: a "Trade"
  option that calls `TradeHelper`, and save on quit / load on startup using
  `CollectionFile`.
- Startup needs to handle a missing or corrupted save file without crashing.
- New tests: the trade menu with bad input (trading a card you don't own,
  trading with yourself), and startup with a missing or garbled save file.

## Next Steps (Aryan)
- If we do the save/load stretch goal, add a way to turn a card into a line
  of text and back (for example `PokemonCard.fromLine(String)`), which must
  reject badly formed lines.
- New tests: parsing a valid line, and a bad-input test for a blank or
  garbled line.

## Attribution rows

| File | Team member | Contribution (how) |
| --- | --- | --- |
| `InputReader.java` | Fernando | Wrote with GenAI assistance; reviewed and tested |
| `InputReaderTest.java` | Fernando | Wrote with GenAI assistance; ran and verified |
| `CollectionApp.java` | Fernando | Wrote with GenAI assistance; reviewed and tested |
| `CollectionAppTest.java` | Fernando | Wrote with GenAI assistance; ran and verified |
| `Rarity.java` | Aryan | Wrote with GenAI assistance; reviewed and tested |
| `EnergyType.java` | Aryan | Wrote with GenAI assistance; reviewed and tested |
| `PokemonCard.java` | Aryan | Wrote with GenAI assistance; reviewed and tested |
| `EnergyCard.java` | Aryan | Wrote with GenAI assistance; reviewed and tested |
| `PokemonCardTest.java` | Aryan | Wrote with GenAI assistance; ran and verified |
| `EnergyCardTest.java` | Aryan | Wrote with GenAI assistance; ran and verified |

**GenAI note (Fernando):** I used Claude to draft `InputReader`,
`CollectionApp`, and their tests from our specification's method signatures
and test plan. I reviewed the code, ran the tests, and checked that each
bad-input case from our scope is handled where the spec says.

**GenAI note (Aryan):** I used Claude to draft the card classes, enums, and
their JUnit tests from our spec's method signatures and test plan. I read
each test to check it tests something real, ran them all, and checked that
the enum order matches InputReader's numbered menus.
