<h1 align="center">💡 progreso</h1>

<div align="center">

![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/ya-ilya/progreso?color=royalblue)
![Build Status](https://img.shields.io/github/actions/workflow/status/ya-ilya/progreso/build.yml?branch=main&logo=gradle)
![Fabric Version](https://img.shields.io/badge/Fabric-1.19.4-informational)

</div>

A Minecraft Utility Mod for anarchy servers

#### ⚠️ Attention

This utility mod not ready to use. So for now it's just an experiment

### Installation

- Install [fabric 1.19.4](https://fabricmc.net/use/installer/)
- Download the latest release from [releases](https://github.com/ya-ilya/progreso/releases) or dev build from [actions](https://github.com/ya-ilya/progreso/actions)
- Put the jar to `.minecraft/mods` folder
- Run [fabric 1.19.4]() from your launcher

### Development

- `progreso-api` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-api)) - Api for `progreso-client`. Shouldn't interact with **minecraft**/**modding api** classes.
- `progreso-client` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-client)) - Client. Uses `progreso-api` as base.
- `progreso-irc-server` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-irc-server)) - Implementation of IRCServer.