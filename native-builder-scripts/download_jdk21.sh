set -e

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

COMPUTED_CHECKSUM=$(sha256sum --check $CSUM)
VALID_CSUM=${COMPUTED_CHECKSUM##*: }

if [[ "$VALID_CSUM" != "OK" ]] ; then
  echo "🫆 Invalid checksum: $COMPUTED_CHECKSUM"
  exit -1
fi

echo "📦 Extracting..."
tar -xzf "$FILE"

DIR=$(ls -d jdk-21* | head -n 1)

echo "📁 Installed in: $DIR"


