# KronHUD Port (unofficial, for 1.21.11)

This is a rebuilt subset of DarkKronicle's KronHUD, targeting Minecraft 1.21.11 on Fabric.
The original mod is archived and depends on a separate library (DarkKore) that is also
archived and was never updated past 1.20.2, so this port drops that dependency and
reimplements config storage and the HUD-edit screen from scratch.

## What's included (Stage 1)
FPS, Ping, Coordinates, Real-world Clock, In-game Clock, Reach Distance, Speed,
Arrow Counter, Active Potion Effects, Armor Durability, Compass.

## Not included yet (Stage 2 - needs mixins into internal MC classes, higher risk)
Keystrokes, Item Update popups, TPS, Combo counter, Toggle-sprint indicator,
and repositioning the vanilla Crosshair/Scoreboard/Boss bar/Action bar.

## Controls
Press `,` (comma) in-game to open the HUD editor.
- Left-click and drag a widget to move it
- Scroll over a widget to resize it
- Right-click a widget to turn it on/off
- Esc to save and close

## Build
Needs JDK 21 and Gradle 9.2+: `gradle build`, jar at `build/libs/kronhudport-1.0.0.jar`.
Or push to GitHub and use the included Actions workflow.

## Known risk areas
This was written without access to a Java compiler or a running Minecraft instance,
so some API names for 1.21.11 (armor inventory access, the new Matrix3x2fStack-based
DrawContext transform methods) are based on documentation/search, not a verified build.
If the build fails, send the error and it can be fixed.
