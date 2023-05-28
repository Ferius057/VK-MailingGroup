package kz.ferius_057.mailingGroup.util;

/**
 * @author Ferius_057 (Charles_Grozny)
 * @date ⭐ 26.05.2023 | 0:21 ⭐
 *
 * <br> Вырезано из maven-artifact
 */
public interface ArtifactVersion extends Comparable<ArtifactVersion> {
    int getMajorVersion();

    int getMinorVersion();

    int getIncrementalVersion();

    int getBuildNumber();

    String getQualifier();

    void parseVersion(String version);
}