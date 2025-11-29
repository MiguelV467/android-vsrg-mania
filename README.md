# Android VSRG Mania 🎮🎵

A mobile Vertical Scrolling Rhythm Game (VSRG) for Android, inspired by osu!mania. Import and play osu!mania beatmaps directly on your Android device!

## Features ✨

- ✅ **osu!mania Beatmap Support**: Import and parse `.osu` beatmap files
- ✅ **Multi-key Support**: Play 4K, 5K, 6K, 7K maps (based on CircleSize)
- ✅ **Long Notes**: Full support for hold notes
- ✅ **Touch Controls**: Multi-touch support for responsive gameplay
- ✅ **Real-time Scoring**: Score tracking with combo system
- ✅ **60 FPS Gameplay**: Smooth rendering and game loop

## Project Structure 📁

```
android-vsrg-mania/
├── app/
│   ├── src/main/
│   │   ├── java/com/vsrg/mania/
│   │   │   ├── MainActivity.kt          # Main activity and UI
│   │   │   ├── GameView.kt              # Game rendering surface
│   │   │   ├── GameEngine.kt            # Core game logic
│   │   │   ├── OsuBeatmapParser.kt      # .osu file parser
│   │   │   ├── Note.kt                  # Note data model
│   │   │   └── Lane.kt                  # Lane/column model
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml    # Main UI layout
│   │   │   └── values/
│   │   │       ├── strings.xml
│   │   │       └── colors.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## Getting Started 🚀

### Prerequisites

- Android Studio Arctic Fox or newer
- Android SDK 24+ (Android 7.0)
- Kotlin 1.9.0+

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/MiguelV467/android-vsrg-mania.git
   cd android-vsrg-mania
   ```

2. **Open in Android Studio**:
   - File → Open → Select project folder
   - Wait for Gradle sync to complete

3. **Build and Run**:
   - Connect an Android device or start an emulator
   - Click Run (▶️) or press Shift+F10

## How to Play 🎮

### Adding Beatmaps

1. Transfer `.osu` beatmap files to your device:
   ```
   /storage/emulated/0/osu/Songs/[Song Folder]/[difficulty].osu
   ```

2. In the app:
   - Tap **"Select Beatmap"**
   - Choose your `.osu` file
   - Tap **"Start Game"** to begin

### Controls

- **Touch the lanes** to hit notes as they reach the green judgment line
- **Hold for long notes** (yellow notes)
- Each lane corresponds to a column in the beatmap

### Scoring

- **Perfect hit**: ±100ms timing window
- **Score**: 300 points per note
- **Combo**: Build combos for consistent hits
- **Miss**: Reset combo if notes pass without hitting

## Technical Details 🔧

### Beatmap Parsing

The `OsuBeatmapParser` reads standard `.osu` files and extracts:
- Metadata (title, artist, creator)
- Difficulty settings (key count from CircleSize)
- Hit objects (notes with timing and lane position)
- Long notes (hold notes with duration)

### Game Engine

- **Scroll Speed**: Configurable note scroll velocity
- **Judgment Line**: 85% down the screen
- **FPS**: Locked at 60 FPS for smooth gameplay
- **Touch Handling**: Multi-touch support for simultaneous lane presses

### Performance

- Efficient canvas rendering
- Object pooling for note rendering
- Minimal garbage collection during gameplay

## TODO / Roadmap 📋

- [ ] File picker implementation for beatmap selection
- [ ] Audio playback synchronization
- [ ] Timing offset calibration
- [ ] Customizable skins and note colors
- [ ] Replay system
- [ ] Online leaderboards
- [ ] More judgment grades (Perfect, Great, Good, Bad, Miss)
- [ ] Visual effects and animations
- [ ] Settings menu (scroll speed, skin, offset)

## Known Limitations ⚠️

- Audio playback not yet implemented
- File picker shows hardcoded path (needs implementation)
- No visual feedback for judgment accuracy
- Limited customization options

## Contributing 🤝

Contributions are welcome! Please feel free to submit issues or pull requests.

## License 📄

This project is open source and available under the MIT License.

## Acknowledgments 🙏

- Inspired by [osu!](https://osu.ppy.sh/) and osu!mania
- Thanks to the osu! community for beatmap format documentation

## Contact 📧

Created by [@MiguelV467](https://github.com/MiguelV467)

---

**Enjoy the rhythm!** 🎵✨
