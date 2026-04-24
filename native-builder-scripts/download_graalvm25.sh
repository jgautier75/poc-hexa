set -e

echo "🔍 Fetching latest GraalVM JDK 25..."

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
  echo "🫆 Invalid checksum: file [$COMPUTED_CHECKSUM] sha256 [$CHECKSUM_CONTENT]"
  exit -1
  else
    echo "🫆 Valid checksum"
fi
#echo "⬇️ CHECKSUM_FILE: [$CHECKSUM_FILE]"
#VALID_CSUM=${COMPUTED_CHECKSUM##*: }
#echo "🫆 $JDK_ARCHIVE checksum: $COMPUTED_CHECKSUM <> checksum content: [$CHECKSUM_CONTENT]"

echo "📦 Extracting..."
tar -xzf "$JDK_ARCHIVE"

DIR=$(ls -d graalvm-community-openjdk-25* | head -n 1)
echo "📁 Installed in: $DIR"
