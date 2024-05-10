This project uses the [Kobweb](https://github.com/varabyte/kobweb) framework under the hood, which is a modern,
Kotlin-based web framework that leverages the power of Compose HTML for building web applications.

## Development Setup

First, run the development server by typing the following command in a terminal under the `site` folder:

```bash
$ cd site
$ kobweb run
```

Open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

You can use any editor you want for the project, but using **IntelliJ IDEA Community Edition** is highly recommended.

Press `Q` in the terminal to gracefully stop the server.

## Exporting the Project

To export the final static output, shutdown the development server and then export the project using:

```bash
$ kobweb export --layout static
```

When finished, you can run a Kobweb server in production mode to test it.

```bash
$ kobweb run --env prod --layout static
```

The above export generates a layout which is compatible with any static hosting provider of your choice, such as
GitHub Pages, Netlify, Firebase, etc. There is also a more powerful export option to create a fullstack server,
but as the additional power provided by that approach is not needed by most users and comes with more expensive
hosting costs, it is not demonstrated in this project.
