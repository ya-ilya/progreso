<h1 align="center">💡 progreso</h1>

<div align="center">

[![Latest Version](https://img.shields.io/github/v/release/ya-ilya/progreso?logo=github)](https://github.com/ya-ilya/progreso/releases/latest)
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/ya-ilya/progreso?color=royalblue)](https://www.codefactor.io/repository/github/ya-ilya/progreso)
[![Build Status](https://img.shields.io/github/actions/workflow/status/ya-ilya/progreso/build.yml?branch=main&logo=gradle)](https://github.com/ya-ilya/progreso/actions)
[![Fabric Version](https://img.shields.io/badge/Fabric-1.21-informational)](https://fabricmc.net/)

</div>

A Minecraft Utility Mod for anarchy servers

### Installation

- Install [fabric 1.21](https://fabricmc.net/use/installer/)
- Download the latest release from [releases](https://github.com/ya-ilya/progreso/releases) or dev build from [actions](https://github.com/ya-ilya/progreso/actions)
- Put the jar to `.minecraft/mods` folder
- Run [fabric 1.21](https://fabricmc.net/use/installer/) from your launcher

### Launching IRC server

- Clone the repository
- Navigate to the project directory
- Rename `.env.example` to `.env` and set `SERVER_PORT`
- Run the following command to start the IRC server using Docker Compose:
  ```sh
  docker-compose up -d
  ```
- The IRC server will be available at `http://localhost:${SERVER_PORT}`

### Development

- `progreso-api` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-api)) - Api for `progreso-client`. Shouldn't interact with **minecraft**/**modding api** classes.
- `progreso-client` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-client)) - Client. Uses `progreso-api` as base.
- `progreso-irc` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-irc)) - IRC server and client.

### Links

- [progreso-plugin-template](https://github.com/ya-ilya/progreso-plugin-template)