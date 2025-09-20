# Test Suite for CopySelectionReferenceAction Plugin

This directory contains comprehensive tests for the CopySelectionReferenceAction IntelliJ IDEA plugin.

## Test Structure

### Unit Tests
- **`CopySelectionReferenceActionTest.kt`** - Comprehensive unit tests with mocked dependencies
- **`CopySelectionReferenceActionSimpleTest.kt`** - Simplified unit tests focusing on core functionality
- **`CopySelectionReferenceActionPerformanceTest.kt`** - Performance tests to ensure the action executes efficiently

### Integration Tests
- **`CopySelectionReferenceActionIntegrationTest.kt`** - Integration tests using IntelliJ's test framework

### Test Utilities
- **`TestUtils.kt`** - Utility functions for creating test data and mock objects
- **`test-config.xml`** - Test configuration and sample data

## Running Tests

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Classes
```bash
# Run unit tests only
./gradlew test --tests "*UnitTest"

# Run integration tests only
./gradlew test --tests "*IntegrationTest"

# Run performance tests only
./gradlew test --tests "*PerformanceTest"
```

### Run Tests with Coverage
```bash
./gradlew test jacocoTestReport
```

## Test Coverage

The test suite covers:

### Functional Testing
- ✅ Cursor position copying (no selection)
- ✅ Single-line selection copying
- ✅ Multi-line selection copying
- ✅ File path handling (relative vs absolute)
- ✅ Edge cases (null values, empty selections)

### Error Handling
- ✅ Null project handling
- ✅ Null editor handling
- ✅ Null virtual file handling
- ✅ Null project base path handling

### Performance Testing
- ✅ Execution time validation
- ✅ Multiple rapid calls efficiency
- ✅ Large file handling

### Integration Testing
- ✅ Real IntelliJ environment testing
- ✅ Clipboard integration
- ✅ File system integration

## Test Data

Test files are created dynamically in the integration tests:
- Kotlin files (`.kt`)
- Java files (`.java`)
- Various file structures and content

## Dependencies

The tests use the following testing frameworks:
- **JUnit 5** - Main testing framework
- **Mockito** - Mocking framework
- **IntelliJ Platform Test Framework** - Integration testing
- **Kotlin Test** - Additional assertions

## Notes

- Integration tests require the IntelliJ Platform test framework
- Performance tests validate execution time thresholds
- Mock tests ensure isolated unit testing
- All tests are designed to be deterministic and repeatable