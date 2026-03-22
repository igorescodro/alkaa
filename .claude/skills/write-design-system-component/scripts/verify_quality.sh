#!/bin/bash
# Verifies Kuvio component quality: compilation and static analysis.

echo "=== Compilation check ==="
./gradlew :libraries:designsystem:compileKotlinDesktop 2>&1 | tail -30

echo "=== Static analysis ==="
./gradlew :libraries:designsystem:detektCommonMainSourceSet ktlintCheck 2>&1 | grep -E "\.kt:|BUILD|FAILED"
