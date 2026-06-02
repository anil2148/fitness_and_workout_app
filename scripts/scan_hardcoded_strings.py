#!/usr/bin/env python3
"""Report likely hardcoded user-facing strings in Compose Kotlin source files."""

import argparse
import re
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "app" / "src" / "main" / "java"
PATTERNS = (
    re.compile(r'\bText\s*\(\s*"([^"]+)"'),
    re.compile(r'\bSnackbar\s*\(\s*"([^"]+)"'),
    re.compile(r'\bToast\.makeText\s*\([^,]+,\s*"([^"]+)"'),
    re.compile(r'\bTopAppBar\s*\(\s*"([^"]+)"'),
)


def is_user_facing(candidate: str) -> bool:
    without_interpolation = re.sub(r"\$\{[^}]+\}|\$\w+", "", candidate)
    without_interpolation = re.sub(r"\\[nrt]", "", without_interpolation)
    return bool(re.search(r"[A-Za-z]", without_interpolation)) and not re.search(
        r"https?://|^[a-z0-9_./:-]+$", without_interpolation.strip()
    )


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--strict", action="store_true", help="Fail when findings are reported.")
    args = parser.parse_args()
    findings: list[tuple[Path, int, str]] = []

    for path in sorted(SOURCE.rglob("*.kt")):
        for line_number, line in enumerate(path.read_text(encoding="utf-8").splitlines(), start=1):
            for pattern in PATTERNS:
                for match in pattern.finditer(line):
                    candidate = match.group(1)
                    if is_user_facing(candidate):
                        findings.append((path.relative_to(ROOT), line_number, candidate))

    if findings:
        print("Likely hardcoded user-facing strings:")
        for path, line_number, text in findings:
            print(f"  {path}:{line_number}: {text!r}")
            print("    Suggested action: move visible copy into res/values/strings.xml.")
    else:
        print("No likely direct Compose hardcoded strings found.")

    print(f"Hardcoded-string scan complete: {len(findings)} finding(s).")
    return 1 if args.strict and findings else 0


if __name__ == "__main__":
    raise SystemExit(main())
