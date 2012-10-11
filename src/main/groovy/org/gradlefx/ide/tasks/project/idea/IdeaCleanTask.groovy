/*
* Copyright (c) 2011 the original author or authors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.gradlefx.ide.tasks.project.idea

import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradlefx.ide.tasks.project.CleanTask

@Mixin(IdeaUtil)
class IdeaCleanTask extends DefaultTask implements CleanTask {

    protected static final IDE_NAME = 'Intellij IDEA'

    public static final NAME = 'ideaClean'

    IdeaCleanTask() {
        description = "Cleans $IDE_NAME project, i.e. removes $IDE_NAME configuration files and folders"
    }

    private Boolean deleteFile(File file) {
        if (file.exists()) {
            logger.info("\\t$file.name")

            file.isFile() ? file.delete() : file.deleteDir()

            return true
        }

        false
    }

    @Override
    @TaskAction
    void cleanProject() {
        logger.info "Removing $IDE_NAME project files"

        boolean filesDeleted = false

        List filesToDelete = [
                IDEA_PROJECT_DIR,
                OUTPUT_DIR
        ].collect {
            project.file it
        }

        filesToDelete << project.projectDir.traverse(type: FileType.FILES, nameFilter: ~/.*\.groovy/)

        println filesToDelete

        filesToDelete.each {
            if (deleteFile(it)) {
                filesDeleted = true
            }
        }

        if (!filesDeleted) {
            logger.info "\tNothing to remove"
        }
    }
}
