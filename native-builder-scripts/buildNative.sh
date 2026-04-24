#!/bin/bash

set -e

JDK_21_NAME=""
GRAALVM_25_NAME=""
MAVEN_NAME=""

downloadMaven(){
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

  DIR=$(ls -d apache-maven-* | head -n 1)
  MAVEN_NAME=$DIR
}

downloadJDK21(){
  echo "🔍 Fetching latest Temurin JDK 21..."

  API_URL="https://api.adoptium.net/v3/assets/latest/21/hotspot"

  DOWNLOAD_CHECKSUM=$(curl -s https://api.adoptium.net/v3/assets/latest/21/hotspot | jq -r '.[] | select(.version.optional=="LTS" and .binary.image_type=="jdk" and .binary.architecture=="x64" and .binary.os=="linux") | .binary.package.checksum_link')
  DOWNLOAD_URL=$(curl -s https://api.adoptium.net/v3/assets/latest/21/hotspot | jq -r '.[] | select(.version.optional=="LTS" and .binary.image_type=="jdk" and .binary.architecture=="x64" and .binary.os=="linux") | .binary.package.link')

  echo "⬇️ Download URL: $DOWNLOAD_URL"
  FILE=${DOWNLOAD_URL##*/}
  curl -L -o "$FILE" "$DOWNLOAD_URL"

  echo "🫆 Checksum URL $DOWNLOAD_CHECKSUM"
  CSUM=${DOWNLOAD_CHECKSUM##*/}
  curl -L -o "$CSUM" "$DOWNLOAD_CHECKSUM"

  echo "🫆 Verifying checksum"
  COMPUTED_CHECKSUM=$(sha256sum --check $CSUM)
  VALID_CSUM=${COMPUTED_CHECKSUM##*: }

  if [[ "$VALID_CSUM" != "OK" ]] ; then
    echo "❌ Invalid checksum: $COMPUTED_CHECKSUM"
    exit -1
  else
    echo "✅ Valid checksum"
  fi

  echo "📦 Extracting..."
  tar -xzf "$FILE"

  DIR=$(ls -d jdk-21* | head -n 1)

  JDK_21_NAME=$DIR
}

downloadGraalVM25(){
  API_URL="https://api.github.com/repos/graalvm/graalvm-ce-builds/releases/latest"

  BROWSER_URL=$(curl -s $API_URL | jq -r '.assets.[] | .browser_download_url' | grep "linux-x64_bin")

  BINARY_URL=""
  CHECKSUM_URL=""

  for durl in $BROWSER_URL; do
    FEXTENSION=${durl##*.}
    #echo "durl: [$durl], extension: [$FEXTENSION]"
    if [[ "$FEXTENSION" == "gz" ]] ; then
      BINARY_URL=$durl
    elif [[ "$FEXTENSION" == "sha256" ]] ; then
      CHECKSUM_URL=$durl
    fi
  done

  echo "⬇️ Download GraalVM JDK 25 url $BINARY_URL"
  JDK_ARCHIVE=${BINARY_URL##*/}
  curl -L -o "$JDK_ARCHIVE" "$BINARY_URL"

  echo "⬇️ Download GraalVM JDK 25 checksum url $CHECKSUM_URL"
  CHECKSUM_FILE=${CHECKSUM_URL##*/}
  curl -L -o "$CHECKSUM_FILE" "$CHECKSUM_URL"
  CHECKSUM_CONTENT=$(cat $CHECKSUM_FILE)

  echo "🫆 Verifying checksum"
  COMPUTED_CHECKSUM=$(sha256sum $JDK_ARCHIVE | cut -d ' ' -f1)
  if [[ "$COMPUTED_CHECKSUM" != "$CHECKSUM_CONTENT" ]] ; then
    echo "❌ Invalid checksum: file [$COMPUTED_CHECKSUM] sha256 [$CHECKSUM_CONTENT]"
    exit -1
    else
      echo "✅ Valid checksum"
  fi

  echo "📦 Extracting..."
  tar -xzf "$JDK_ARCHIVE"

  DIR=$(ls -d graalvm-community-openjdk-25* | head -n 1)

  GRAALVM_25_NAME=$DIR
}

downloadJDK21
downloadGraalVM25
downloadMaven

sed -e "s/JDK21BIN/${JDK_21_NAME}/g" -e "s/GRAALVM25/${GRAALVM_25_NAME}/g" /root/.m2/template_toolchains.xml > /root/.m2/toolchains.xml

export JAVA_HOME="/java-sources/$GRAALVM_25_NAME"
export PATH=$PATH:$JAVA_HOME/bin

echo "/java-sources/${MAVEN_NAME}/bin/mvn clean install -DskipTests -Pnative"

/java-sources/${MAVEN_NAME}/bin/mvn clean install -DskipTests -Pnative
