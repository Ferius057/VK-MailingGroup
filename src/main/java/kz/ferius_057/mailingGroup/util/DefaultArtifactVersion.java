package kz.ferius_057.mailingGroup.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.StringTokenizer;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 26.05.2023 | 0:21 ⭐
 *
 * <br> Вырезано из maven-artifact
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DefaultArtifactVersion implements ArtifactVersion {
    Integer majorVersion, minorVersion, incrementalVersion, buildNumber;

    String qualifier;

    ComparableVersion comparable;

    public DefaultArtifactVersion(String version) {
        parseVersion(version);
    }

    @Override
    public int hashCode() {
        return 11 + comparable.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof ArtifactVersion)) {
            return false;
        }

        return compareTo((ArtifactVersion) other) == 0;
    }

    public int compareTo(@NotNull ArtifactVersion otherVersion) {
        if (otherVersion instanceof DefaultArtifactVersion) {
            return this.comparable.compareTo(((DefaultArtifactVersion) otherVersion).comparable);
        } else {
            return compareTo(new DefaultArtifactVersion(otherVersion.toString()));
        }
    }

    public int getMajorVersion() {
        return majorVersion != null ? majorVersion : 0;
    }

    public int getMinorVersion() {
        return minorVersion != null ? minorVersion : 0;
    }

    public int getIncrementalVersion() {
        return incrementalVersion != null ? incrementalVersion : 0;
    }

    public int getBuildNumber() {
        return buildNumber != null ? buildNumber : 0;
    }

    public String getQualifier() {
        return qualifier;
    }

    public final void parseVersion(String version) {
        comparable = new ComparableVersion(version);

        int index = version.indexOf('-');

        String part1;
        String part2 = null;

        if (index < 0) {
            part1 = version;
        } else {
            part1 = version.substring(0, index);
            part2 = version.substring(index + 1);
        }

        if (part2 != null) {
            if (part2.length() == 1 || !part2.startsWith("0")) {
                buildNumber = tryParseInt(part2);
                if (buildNumber == null) {
                    qualifier = part2;
                }
            } else {
                qualifier = part2;
            }
        }

        if ((!part1.contains(".")) && !part1.startsWith("0")) {
            majorVersion = tryParseInt(part1);
            if (majorVersion == null) {
                // qualifier is the whole version, including "-"
                qualifier = version;
                buildNumber = null;
            }
        } else {
            boolean fallback = false;

            StringTokenizer tok = new StringTokenizer(part1, ".");
            if (tok.hasMoreTokens()) {
                majorVersion = getNextIntegerToken(tok);
                if (majorVersion == null) {
                    fallback = true;
                }
            } else {
                fallback = true;
            }
            if (tok.hasMoreTokens()) {
                minorVersion = getNextIntegerToken(tok);
                if (minorVersion == null) {
                    fallback = true;
                }
            }
            if (tok.hasMoreTokens()) {
                incrementalVersion = getNextIntegerToken(tok);
                if (incrementalVersion == null) {
                    fallback = true;
                }
            }
            if (tok.hasMoreTokens()) {
                qualifier = tok.nextToken();
                fallback = isDigits(qualifier);
            }

            // string tokenizer won't detect these and ignores them
            if (part1.contains("..") || part1.startsWith(".") || part1.endsWith(".")) {
                fallback = true;
            }

            if (fallback) {
                // qualifier is the whole version, including "-"
                qualifier = version;
                majorVersion = null;
                minorVersion = null;
                incrementalVersion = null;
                buildNumber = null;
            }
        }
    }

    private static boolean isDigits(String cs) {
        if (cs == null || cs.isEmpty()) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Integer getNextIntegerToken(StringTokenizer tok) {
        String s = tok.nextToken();
        if ((s.length() > 1) && s.startsWith("0")) {
            return null;
        }
        return tryParseInt(s);
    }

    private static Integer tryParseInt(String s) {
        // for performance, check digits instead of relying later on catching NumberFormatException
        if (!isDigits(s)) {
            return null;
        }

        try {
            long longValue = Long.parseLong(s);
            if (longValue > Integer.MAX_VALUE) {
                return null;
            }
            return (int) longValue;
        } catch (NumberFormatException e) {
            // should never happen since checked isDigits(s) before
            return null;
        }
    }

    @Override
    public String toString() {
        return comparable.toString();
    }
}