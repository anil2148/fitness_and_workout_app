#!/usr/bin/env python3
"""Verify that every supported Android locale contains the default string keys."""

from collections import Counter
from pathlib import Path
from xml.etree import ElementTree


ROOT = Path(__file__).resolve().parents[1]
RESOURCES = ROOT / "app" / "src" / "main" / "res"
DEFAULT = RESOURCES / "values" / "strings.xml"
LOCALES = ("hi", "es", "fr", "ar")


def read_keys(path: Path) -> list[str]:
    root = ElementTree.parse(path).getroot()
    return [
        element.attrib["name"]
        for element in root
        if element.tag == "string" and element.attrib.get("translatable", "true") != "false"
    ]


def duplicates(keys: list[str]) -> list[str]:
    return sorted(key for key, count in Counter(keys).items() if count > 1)


def main() -> int:
    default_keys = read_keys(DEFAULT)
    expected = set(default_keys)
    failed = False

    duplicate_defaults = duplicates(default_keys)
    if duplicate_defaults:
        failed = True
        print(f"Duplicate default keys: {', '.join(duplicate_defaults)}")

    print(f"Default locale: {len(expected)} translatable string keys")
    for locale in LOCALES:
        path = RESOURCES / f"values-{locale}" / "strings.xml"
        keys = read_keys(path)
        actual = set(keys)
        missing = sorted(expected - actual)
        extra = sorted(actual - expected)
        duplicate_keys = duplicates(keys)
        print(f"{locale}: {len(actual)} keys")
        print(f"  missing: {', '.join(missing) if missing else 'none'}")
        print(f"  extra: {', '.join(extra) if extra else 'none'}")
        print(f"  duplicates: {', '.join(duplicate_keys) if duplicate_keys else 'none'}")
        if missing or duplicate_keys:
            failed = True

    if failed:
        print("String verification failed.")
        return 1

    print("String verification passed.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
