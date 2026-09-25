# FreeGive

Two separate, full Gradle mods that add a permissionless `/freegive` command.

## Commands

```text
/freegive <item> <count>
```

Examples:

```text
/freegive minecraft:diamond 1
/freegive minecraft:stone 64
```

The command intentionally uses the name `freegive`. Registering a second `/give`
literal would collide with the vanilla command and would not reliably remove its
permission requirement. The command is available to players without operator
permission because neither implementation adds a `.requires(...)` predicate.

## Projects

### Forge 1.20.1

Project: [forge-1.20.1](forge-1.20.1)

Run `gradle build` inside that directory with Gradle 8.8 and Java 17. The finished jar is written to
`forge-1.20.1/build/libs/`.

### Fabric 1.21.11

Project: [fabric-1.21.11](fabric-1.21.11)

Run `gradle build` inside that directory with Java 21. The finished jar is written to
`fabric-1.21.11/build/libs/`.

The Forge project uses Java 17. The Fabric 1.21.11 project uses Java 21.

## Entrypoints

- Forge: [ExampleMod.java](forge-1.20.1/src/main/java/com/example/examplemod/ExampleMod.java)
- Fabric: [FreeGiveMod.java](fabric-1.21.11/src/main/java/com/example/modid/FreeGiveMod.java)
