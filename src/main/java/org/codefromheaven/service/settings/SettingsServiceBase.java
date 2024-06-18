package org.codefromheaven.service.settings;

import org.codefromheaven.dto.BaseSetting;
import org.codefromheaven.dto.FileType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public abstract class SettingsServiceBase {

    protected static final String DELIMITER = ";";

    protected static boolean isPresentMyOwnSettingFile(FileType fileType) {
        try (BufferedReader ignored = new BufferedReader(new FileReader(fileType.getPersonalizedConfigName()))) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String getVariable(BaseSetting setting) {
        Optional<String> value = getVariableBase(setting, setting.getElementType().getPersonalizedConfigName());
        return value.orElseGet(() -> getVariableBase(setting, setting.getElementType().getDefaultFileName()).get());
    }

    private static Optional<String> getVariableBase(BaseSetting setting, String settingCsvPath) {
        try (BufferedReader br = new BufferedReader(new FileReader(settingCsvPath))) {
            String line;
            // skip first line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                if (setting.getName().equals(values[0])) {
                    return Optional.of(values[1]);
                }
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

}
