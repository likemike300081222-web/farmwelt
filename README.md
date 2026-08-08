# FarmWorld Plugin

Erzeugt eine eigene Welt mit biomspezifischen Custom-Bäumen und kleinen,
zufällig generierten Dungeons im Untergrund.

## Features
- `/farmworld create <name>` – erstellt eine neue Farmwelt
- `/farmworld tp <name>` – teleportiert dorthin
- `/farmworld list` – zeigt geladene Welten
- 6 eigene "Biome" (Plains, Forest, Desert, Taiga, Swamp, Savanna) in großen
  Zonen verteilt (Größe konfigurierbar), jedes mit eigenem Baumtyp:
  - Plains: "Golden Tree" mit Glowstone-Spitze
  - Forest: hoher Redwood
  - Desert: geneigte Palme
  - Taiga: Frost-Baum mit Schneehaube
  - Swamp: Mangrove
  - Savanna: breite Schirm-Krone
- Zufällige Mini-Dungeons unter der Erde: Raum aus bemoosten Bruchsteinziegeln,
  Monster-Spawner (zufälliger Mob) + Truhe mit Vanilla-Dungeon-Loot-Table

## Konfiguration (config.yml)
```yaml
tree-chance-per-chunk: 0.35
dungeon-chance-per-chunk: 0.02
dungeon-min-y: -40
dungeon-max-y: 10
biome-zone-size: 200
```

## Bauen
Voraussetzung: Java 21+ und Maven (Paper 1.21+ setzt Java 21 voraus).

```bash
mvn clean package
```

Die fertige JAR liegt danach unter `target/farmworld-1.0.0.jar`.
Wichtig: Der Build lädt die Paper-API automatisch vom PaperMC-Repository
herunter (repo.papermc.io) – das Sandbox-Environment hier hatte darauf
keinen Netzwerkzugriff, daher konnte ich es nicht selbst kompilieren.
Baue es bitte lokal mit deiner IDE (IntelliJ mit Maven-Support reicht) oder
per `mvn clean package` auf der Kommandozeile.

## Installation
1. JAR in den `plugins`-Ordner deines Paper-Servers legen
2. Server starten/neu laden
3. `/farmworld create meinefarm` ausführen

## Erweiterungsideen
- Mehr Biome/Baumformen in `FarmBiome` und `TreePopulator` ergänzen
- Größere/variablere Dungeon-Layouts (mehrere Räume, Gänge) in `DungeonPopulator`
- Sanftere Übergänge zwischen Biom-Zonen (aktuell scharfe Zellgrenzen) über
  echtes Perlin/Simplex-Noise statt reiner Zell-Hashes
- Eigene Loot-Tables statt der Vanilla-"Simple Dungeon"-Tabelle
