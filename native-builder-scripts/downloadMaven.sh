#!/usr/bin/env bash
set -euo pipefail

BASE_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven"
METADATA_URL="$BASE_URL/maven-metadata.xml"

# Option: définir une major (ex: 3 ou 4), sinon vide = toutes
MAJOR_FILTER="${3:-}"

echo "🌐 Fetch versions ..."

VERSIONS=$(curl -s "$METADATA_URL" \
  | grep -oP '(?<=<version>)[^<]+' \
  | grep -Ev '(?i)(rc|alpha|beta|milestone|m[0-9]+|snapshot)' \
)

# Filtrer format strict X.Y.Z
VERSIONS=$(echo "$VERSIONS" | grep -E '^[0-9]+\.[0-9]+\.[0-9]+$')

# Major version filter
if [[ -n "$MAJOR_FILTER" ]]; then
  VERSIONS=$(echo "$VERSIONS" | grep "^$MAJOR_FILTER\.")
fi

LATEST_VERSION=$(echo "$VERSIONS" | sort -V | tail -1)

if [[ -z "$LATEST_VERSION" ]]; then
  echo "No valid version found"
  exit 1
fi

echo "Latest stable version : $LATEST_VERSION"

ARCHIVE="apache-maven-$LATEST_VERSION-bin.tar.gz"
URL="$BASE_URL/$LATEST_VERSION/$ARCHIVE"
MAVEN_ARCHIVE=${URL##*/}
SHA_URL="$URL.sha512"
CHECKSUM_FILE=${SHA_URL##*/}

echo "⬇️ Donwload $ARCHIVE ..."
curl -o "$MAVEN_ARCHIVE" "$URL"
curl -o "$CHECKSUM_FILE" "$SHA_URL"

CHECKSUM_CONTENT=$(cat $CHECKSUM_FILE)

echo "🫆 Verify checksum"
COMPUTED_CHECKSUM=$(sha512sum $MAVEN_ARCHIVE | cut -d ' ' -f1)
if [[ "$COMPUTED_CHECKSUM" != "$CHECKSUM_CONTENT" ]] ; then
  echo "🫆 Invalid checksum: file [$COMPUTED_CHECKSUM] sha256 [$CHECKSUM_CONTENT]"
  exit -1
  else
    echo "🫆 Valid checksum"
fi


echo "📦 Extracting ..."
tar -xzf "$ARCHIVE"

