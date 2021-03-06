/*******************************************************************************
 * Copyright (c) 2012-2017 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.editor.EditorRegistry;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.api.filetypes.FileType;
import org.eclipse.che.ide.api.filetypes.FileTypeRegistry;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.eclipse.che.ide.ext.web.css.NewCssFileAction;
import org.eclipse.che.ide.ext.web.css.NewLessFileAction;
import org.eclipse.che.ide.ext.web.html.NewHtmlFileAction;
import org.eclipse.che.ide.ext.web.html.PreviewAction;
import org.eclipse.che.ide.ext.web.html.editor.HtmlEditorProvider;
import org.eclipse.che.ide.ext.web.js.NewJavaScriptFileAction;
import org.eclipse.che.ide.ext.web.js.editor.JsEditorProvider;

import static org.eclipse.che.ide.api.action.IdeActions.GROUP_ASSISTANT;
import static org.eclipse.che.ide.api.action.IdeActions.GROUP_FILE_NEW;
import static org.eclipse.che.ide.api.action.IdeActions.GROUP_MAIN_CONTEXT_MENU;

/**
 * Extension add editing JavaScript, HTML, CSS css type support to the IDE Application.
 * It provides configured TextEditorView with syntax coloring and autocomplete.
 *
 * @author Nikolay Zamosenchuk
 */
@Singleton
@Extension(title = "Web", version = "3.0.0", description = "syntax highlighting and autocomplete.")
public class WebExtension {
    /**
     * Web Extension adds JavaScript, HTML and CSS Support to IDE Application.
     * It provides syntax highlighting for CSS, JS, HTML files and code completion features for CSS files to IDE.
     */
    @Inject
    public WebExtension(HtmlEditorProvider htmlEditorProvider,
                        JsEditorProvider jsEditorProvider,
                        EditorRegistry editorRegistry,
                        WebExtensionResource resources,
                        IconRegistry iconRegistry,
                        @Named("JSFileType") FileType jsFile,
                        @Named("HTMLFileType") FileType htmlFile,
                        @Named("ES6FileType") FileType es6File,
                        @Named("JSXFileType") FileType jsxFile) {
        // register new Icon for javascript project type
        iconRegistry.registerIcon(new Icon("JavaScript.samples.category.icon", resources.samplesCategoryJs()));

        editorRegistry.registerDefaultEditor(jsFile, jsEditorProvider);
        editorRegistry.registerDefaultEditor(es6File, jsEditorProvider);
        editorRegistry.registerDefaultEditor(jsxFile, jsEditorProvider);
        editorRegistry.registerDefaultEditor(htmlFile, htmlEditorProvider);
    }

    @Inject
    private void registerFileTypes(FileTypeRegistry fileTypeRegistry,
                                   @Named("CSSFileType") FileType cssFile,
                                   @Named("LESSFileType") FileType lessFile,
                                   @Named("JSFileType") FileType jsFile,
                                   @Named("ES6FileType") FileType es6File,
                                   @Named("JSXFileType") FileType jsxFile,
                                   @Named("TypeScript") FileType typeScriptFile,
                                   @Named("HTMLFileType") FileType htmlFile,
                                   @Named("PHPFileType") FileType phpFile) {
        fileTypeRegistry.registerFileType(cssFile);
        fileTypeRegistry.registerFileType(lessFile);
        fileTypeRegistry.registerFileType(jsFile);
        fileTypeRegistry.registerFileType(es6File);
        fileTypeRegistry.registerFileType(jsxFile);
        fileTypeRegistry.registerFileType(typeScriptFile);
        fileTypeRegistry.registerFileType(htmlFile);
        fileTypeRegistry.registerFileType(phpFile);
    }

    @Inject
    private void prepareActions(WebExtensionResource resources,
                                ActionManager actionManager,
                                NewCssFileAction newCssFileAction,
                                NewLessFileAction newLessFileAction,
                                NewHtmlFileAction newHtmlFileAction,
                                NewJavaScriptFileAction newJavaScriptFileAction,
                                PreviewAction previewAction) {
        // register actions
        actionManager.registerAction("newCssFile", newCssFileAction);
        actionManager.registerAction("newLessFile", newLessFileAction);
        actionManager.registerAction("newHtmlFile", newHtmlFileAction);
        actionManager.registerAction("newJavaScriptFile", newJavaScriptFileAction);
        actionManager.registerAction("previewHTML", previewAction);

        // set icons
        newCssFileAction.getTemplatePresentation().setSVGResource(resources.cssFile());
        newLessFileAction.getTemplatePresentation().setSVGResource(resources.lessFile());
        newHtmlFileAction.getTemplatePresentation().setSVGResource(resources.htmlFile());
        newJavaScriptFileAction.getTemplatePresentation().setSVGResource(resources.jsFile());

        // add actions in main menu
        DefaultActionGroup newGroup = (DefaultActionGroup)actionManager.getAction(GROUP_FILE_NEW);
        newGroup.add(newCssFileAction);
        newGroup.add(newLessFileAction);
        newGroup.add(newHtmlFileAction);
        newGroup.add(newJavaScriptFileAction);

        // add actions in context menu
        DefaultActionGroup mainContextMenuGroup = (DefaultActionGroup)actionManager.getAction(GROUP_MAIN_CONTEXT_MENU);
        mainContextMenuGroup.add(previewAction);

        // add actions in Assistant main menu
        DefaultActionGroup assistantMainMenuGroup = (DefaultActionGroup)actionManager.getAction(GROUP_ASSISTANT);
        assistantMainMenuGroup.add(previewAction);
    }
}
