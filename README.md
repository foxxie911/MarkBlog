# MarkBlog

*A modern static blog generator with automated code quality enforcement*

[![Java Version](https://img.shields.io/badge/Java-25-orange.svg)](https://adoptium.net/)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Code Style](https://img.shields.io/badge/code%20style-spotless-brightgreen)](https://github.com/diffplug/spotless)
[![Tests](https://img.shields.io/badge/tests-passing-brightgreen)](https://github.com/foxxie911/MarkBlog)
[![Dependencies](https://img.shields.io/badge/dependencies-up%20to%20date-brightgreen)](https://github.com/foxxie911/MarkBlog)

A modern, lightweight static blog generator that transforms Markdown articles into beautifully formatted HTML websites using Mustache templating.

## Features

- **Markdown to HTML Conversion**: Powered by CommonMark for reliable Markdown parsing
- **Flexible Templating**: Mustache templates for customizable layouts
- **Static Site Generation**: Fast, secure static websites with no runtime dependencies
- **Environment Configuration**: Dotenv support for flexible deployment configurations
- **Modern Java**: Built with Java 25 and contemporary best practices
- **Comprehensive Testing**: Full test coverage with JUnit 5 and Mockito
- **Code Quality Enforcement**: Automated formatting with Spotless plugin and static analysis with Checkstyle
- **Dependency Injection**: Clean architecture using PicoContainer

## Quick Start

### Prerequisites

- Java 25 or higher
- Maven 3.8+
- Git

### Installation

```bash
# Clone the repository
git clone https://github.com/foxxie911/MarkBlog.git
cd MarkBlog

# Build the project with code quality checks
mvn clean install

# Verify code formatting
mvn spotless:check

# Run tests
mvn test
```

### Configuration

Create a `.env` file in your project root:

```env
BLOG_NAME=My Awesome Blog
BLOG_BIO=A blog about technology and development
SITE_PATH=/path/to/output/site
ARTICLE_PATH=/path/to/markdown/articles
```

### Usage

```bash
# Generate your static blog
mvn compile exec:java -Dexec.mainClass="dev.foxxie911.BlogGeneratorApplication"

# Or create and run executable JAR
mvn package
java -jar target/MarkBlog-1.0-SNAPSHOT.jar
```

## Project Structure

```
MarkBlog/
├── src/
│   ├── main/
│   │   ├── java/dev/foxxie911/
│   │   │   ├── BlogGeneratorApplication.java  # Main application class
│   │   │   ├── config/                        # Configuration management
│   │   │   ├── exception/                     # Custom exceptions
│   │   │   ├── models/                        # Data models (Article, ArticleList)
│   │   │   ├── repository/                    # Data access layer
│   │   │   └── service/                       # Business logic services
│   │   └── resources/
│   │       ├── mustaches/                     # Mustache templates
│   │       └── styles/                        # CSS stylesheets
│   └── test/                                  # Unit and integration tests
├── .env                                       # Environment configuration
├── .editorconfig                              # Editor configuration
├── pom.xml                                    # Maven configuration
├── README.md                                  # This file
└── LICENSE                                    # License
```

## Architecture

MarkBlog follows a clean architecture pattern with clear separation of concerns:

### Core Components

- **BlogGeneratorApplication**: Main application orchestrator
- **Article Repository**: Manages article storage and retrieval (FileSystemArticleRepository)
- **Markdown Parser**: Converts Markdown content to HTML using CommonMark (MarkdownParsingService)
- **Template Renderer**: Processes Mustache templates with dynamic data (TemplateRenderingService)
- **Page Generator**: Creates HTML pages from articles and templates (PageGenerationService)
- **Asset Manager**: Handles static resource copying (CSS, fonts, etc.) (AssetManagementService)
- **Configuration**: Environment-based configuration management (BlogConfiguration)

### Dependency Injection

The project uses PicoContainer for dependency injection, providing:
- Loose coupling between components
- Easy testing through mock injection
- Clear component lifecycle management

## Development

### Building and Testing

```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Check code formatting
mvn spotless:check

# Apply automatic formatting fixes
mvn spotless:apply

# Create executable JAR
mvn package

# Install to local repository
mvn install
```

The project includes comprehensive tests covering:
- Unit tests for individual components
- Integration tests for complete workflows
- Mock-based testing for external dependencies

```bash
# Run all tests
mvn test

# Run specific test class
mvn -Dtest=BlogConfigurationTest test

# Generate test coverage report
mvn jacoco:report
```

### Code Quality

```bash
# Check code formatting and import ordering
mvn spotless:check

# Automatically fix formatting issues
mvn spotless:apply

# Run static code analysis (uses simplified configuration)
mvn checkstyle:check

# Run all tests to ensure code quality
mvn test
```

## Contributing

Contributions are welcome! Here's how you can help:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow existing code style and conventions
- Write tests for new functionality
- Run `mvn spotless:check` to ensure proper formatting
- Update documentation as needed
- Ensure all tests pass before submitting PR
- Use descriptive commit messages

## Requirements

### System Requirements

- **Java**: Version 25 or higher
- **Maven**: Version 3.8 or higher
- **Memory**: Minimum 512MB RAM
- **Storage**: Depends on number of articles (typically < 100MB)

### Dependencies

Key dependencies include:
- **Mustache Java**: Template rendering
- **CommonMark**: Markdown parsing
- **Dotenv Java**: Environment configuration
- **SLF4J**: Logging framework
- **PicoContainer**: Dependency injection
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework

See `pom.xml` for complete dependency list.

## Use Cases

MarkBlog is ideal for:

- **Personal Blogs**: Simple, fast static blog generation
- **Documentation Sites**: Technical documentation with clean formatting
- **Portfolio Websites**: Showcase projects with Markdown content
- **Developer Blogs**: Code-focused content with syntax highlighting support
- **Company News**: Internal or external news sites

## Security

- Regular dependency updates to address vulnerabilities
- Secure configuration through environment variables
- No runtime database connections or external service dependencies
- Static file generation reduces attack surface

## Performance

- **Fast Generation**: Processes hundreds of articles in seconds
- **Lightweight Output**: Minimal HTML/CSS footprint
- **Efficient Caching**: Smart template compilation
- **Optimized Streams**: Proper resource management and cleanup

## Troubleshooting

### Common Issues

**Environment Variables Not Loading**
```bash
# Ensure .env file exists in project root
# Check file permissions
# Verify variable names match configuration expectations
```

**Template Rendering Failures**
```bash
# Verify mustache templates exist in src/main/resources/mustaches/
# Check template syntax and variable names
# Ensure proper encoding (UTF-8 recommended)
```

**Missing Dependencies**
```bash
# Run mvn dependency:resolve
# Check Maven repository connectivity
# Verify Java version compatibility
```

## License

This project is licensed under the GPL v3.0 License - see the [LICENSE](LICENSE) file for details.

## Authors

- **foxxie911** - *Initial work* - [foxxie911](https://github.com/foxxie911)

## Acknowledgments

- [CommonMark](https://commonmark.org/) for reliable Markdown parsing
- [Mustache](https://mustache.github.io/) for elegant templating
- [PicoContainer](https://picocontainer.com/) for lightweight DI

## Support

- **Issues**: [GitHub Issues](https://github.com/foxxie911/MarkBlog/issues)
- **Discussions**: [GitHub Discussions](https://github.com/foxxie911/MarkBlog/discussions)

---

⭐ If you find MarkBlog useful, please consider starring the repository!
