# Book4Us

Welcome to Book4Us, an asynchronous second hand book shop that showcases the power of Kotlin and modern web
technologies. This project demonstrates the use of a different asynchronous programming technique than the usual
techniques used in JavaScript-based technologies. This technique is using Coroutines. It's extremely powerful and keeps
the code simple.

## Project Overview

Book4Us is built using a combination of cutting-edge technologies:

+ **Ktor Client:** A powerful HTTP client for Kotlin that enables seamless communication with the remote server to fetch
  book data.
+ **Kotlin Serialization:** A modern serialization library for Kotlin that simplifies the parsing and serialization of
  JSON data.
+ **Kotlin Coroutines:** A concurrency framework for Kotlin that allows for efficient and expressive asynchronous
  programming.
+ **Compose HTML:** A port of Compose Multiplatform that targets HTML as the output, enabling the creation of dynamic
  and interactive web pages.
+ **Kobweb:** A modern, Kotlin-based web framework that leverages the power of Compose HTML for building web
  applications.

## Key Features

### Fetching Data

Book4Us uses suspending functions inside coroutines to fetch book catalog data from a remote server. The application
utilizes Ktor Client, a powerful HTTP client for Kotlin, to make asynchronous network requests and retrieve the JSON
data. By leveraging Kotlin Coroutines, Book4Us ensures that the UI remains responsive during the data fetching process,
providing a seamless user experience.

### Parsing and Displaying Data

Once the book catalog data is fetched from the remote server, Book4Us efficiently parses the JSON data using Kotlinx
Serialization. The parsed data is then dynamically rendered on the webpage using Compose HTML. The application
generates the necessary HTML elements to display the book information in a visually appealing and user-friendly manner.

### Error Handling

Book4Us places a strong emphasis on robust error handling. Each asynchronous method is equipped with appropriate error
handling mechanisms to gracefully handle network errors or parsing failures, as a part of the return type. In case of
any issues, informative error messages are displayed to the user, ensuring a smooth and uninterrupted user experience.

### User Interface

The user interface of Book4Us is designed with simplicity and intuitiveness in mind. The application features a clean
and modern design, making it easy for users to navigate and explore the book catalog. The Home page serves as the main
hub, presenting a list of books with relevant information such as title, author, and genres. In the future, Users will
be able to effortlessly sort the books by title or price to find their desired books quickly.

### Documentation

Comprehensive documentation is provided right here, to guide developers through the implementation details of Book4Us.

### Top Navigation Bar

The Book4Us website features a sticky top navigation bar that remains fixed at the top of the page as the user scrolls
down. This navigation bar provides quick access to essential sections of the website, such as the Home page and About
page, ensuring easy navigation and a seamless user experience. The menu items fold into a hamburger menu on smaller
screens, optimizing the user experience for mobile devices.

### A Funny Hack

The Book API used by the project doesn't provide any price information for the books. Since this is a shop, it needs
price tags. So, the project uses a funny hack to use the hash code of the book title as an input, and then generates a
price based on that hash code. This ensures that each book has a price, and the price remains consistent for the same
book title.

## Pages

The project consists of the following main pages:

+ **Home Page:** The landing page of Book4Us, where users can view the list of books.

+ **About Page:** Provides information about the Book4Us project, its purpose, and the technologies used.

## How to Use

This project uses the [Kobweb](https://github.com/varabyte/kobweb) framework under the hood, which is a modern,
Kotlin-based web framework that leverages the power of Compose HTML for building web applications.

### Development Setup

First, run the development server by typing the following command in a terminal under the `site` folder:

```bash
$ cd site
$ kobweb run
```

Open [http://localhost:8080](http://localhost:8080) with your browser to see the result.

You can use any editor you want for the project, but using **IntelliJ IDEA Community Edition** is highly recommended.

Press `Q` in the terminal to gracefully stop the server.

### Exporting the Project

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
