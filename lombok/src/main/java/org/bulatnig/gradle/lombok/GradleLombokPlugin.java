package org.bulatnig.gradle.lombok;

import io.freefair.gradle.plugins.lombok.LombokExtension;
import io.freefair.gradle.plugins.lombok.LombokPlugin;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class GradleLombokPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(LombokPlugin.class);
        var lombokExtension = project.getExtensions().getByType(LombokExtension.class);
        lombokExtension.getVersion().set("1.18.22");
        project
                .getConfigurations()
                .getByName("implementation")
                .getDependencies()
                .add(project.getDependencies().create("org.projectlombok:lombok:" + lombokExtension.getVersion().get()));
    }
}
