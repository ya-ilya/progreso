<h1 align="center">💡 progreso</h1>

<div align="center">

[![Latest Version](https://img.shields.io/github/v/release/ya-ilya/progreso?logo=github)](https://github.com/ya-ilya/progreso/releases/latest)
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/ya-ilya/progreso?color=royalblue)](https://www.codefactor.io/repository/github/ya-ilya/progreso)
[![Build Status](https://img.shields.io/github/actions/workflow/status/ya-ilya/progreso/build.yml?branch=main&logo=gradle)](https://github.com/ya-ilya/progreso/actions)
[![Fabric Version](https://img.shields.io/badge/Fabric-1.21-informational)](https://fabricmc.net/)

</div>

## Table of contents
- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Launching IRC server](#launching-irc-server)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Links](#links)

## Introduction
Progreso is a Minecraft Utility Mod designed for anarchy servers. It offers a range of features to enhance gameplay and provide utility functions for players.

## Prerequisites
- Java 21 or higher
- Docker and Docker Compose (for running the IRC server)

## Installation
- Install [fabric 1.21](https://fabricmc.net/use/installer/)
- Download the latest release from [releases](https://github.com/ya-ilya/progreso/releases) or dev build from [actions](https://github.com/ya-ilya/progreso/actions)
- Place the downloaded jar file in the `.minecraft/mods` folder
- Launch Minecraft using the [Fabric 1.21 profile](https://fabricmc.net/use/installer/)

## Launching IRC server
- Clone the repository
- Navigate to the project directory
- Rename `.env.example` to `.env` and set `SERVER_PORT`
- Run the following command to start the IRC server using Docker Compose:
  ```sh
  docker-compose up -d
  ```
- The IRC server will be available at `ws://localhost:${SERVER_PORT}`

## Development
- `progreso-api` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-api)) - Api for `progreso-client`. Shouldn't interact with **minecraft**/**modding api** classes.
- `progreso-client` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-client)) - Client. Uses `progreso-api` as base.
- `progreso-irc` ([source](https://github.com/ya-ilya/progreso/tree/main/progreso-irc)) - IRC server and client.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository
2. Create a new branch (`git checkout -b feature-branch`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add new feature'`)
5. Push to the branch (`git push origin feature-branch`)
6. Open a pull request

## License
This project is licensed under the GNU General Public License v3.0. See the [LICENSE.md](LICENSE.md) file for details.

## Contact
For any questions or support, please open an issue on the [GitHub repository](https://github.com/ya-ilya/progreso/issues).

## Links
- [progreso-plugin-template](https://github.com/ya-ilya/progreso-plugin-template)