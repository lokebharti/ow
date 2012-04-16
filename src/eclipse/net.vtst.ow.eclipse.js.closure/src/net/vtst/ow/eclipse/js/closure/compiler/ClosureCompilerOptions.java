package net.vtst.ow.eclipse.js.closure.compiler;

import net.vtst.eclipse.easy.ui.properties.stores.IReadOnlyStore;
import net.vtst.eclipse.easy.ui.properties.stores.LaunchConfigurationReadOnlyStore;
import net.vtst.eclipse.easy.ui.properties.stores.ProjectPropertyStore;
import net.vtst.ow.eclipse.js.closure.OwJsClosurePlugin;
import net.vtst.ow.eclipse.js.closure.launching.ClosureCompilerLaunchConfigurationRecord;
import net.vtst.ow.eclipse.js.closure.properties.ClosureProjectPropertyRecord;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.google.javascript.jscomp.ClosureCodingConvention;
import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JqueryCodingConvention;
import com.google.javascript.jscomp.WarningLevel;

/**
 * This class provides static methods for creating {@code CompilerOptions}.
 * @author Vincent Simonet
 */
public class ClosureCompilerOptions {
  
  // This is based on CommandLineRunner.createOptions() and AbstractCommandLineRunner.setRunOptions()
  private static CompilerOptions makeInternal(IProject project, IReadOnlyStore launchStore, boolean ideMode) throws CoreException {
    ClosureProjectPropertyRecord projectRecord = ClosureProjectPropertyRecord.getInstance();
    ClosureCompilerLaunchConfigurationRecord launchRecord = ClosureCompilerLaunchConfigurationRecord.getInstance();

    // From CommandLineRunner.createOptions()
    CompilerOptions options = new CompilerOptions();
    IReadOnlyStore projectStore = new ProjectPropertyStore(project, OwJsClosurePlugin.PLUGIN_ID);
    options.setCodingConvention(new ClosureCodingConvention());
    CompilationLevel level = CompilationLevel.WHITESPACE_ONLY;
    if (!ideMode) {
       level.setOptionsForCompilationLevel(options);
       if (launchRecord.generateExports.get(launchStore)) {
         options.setGenerateExports(true);
       }
    }

    WarningLevel wLevel = projectRecord.warningLevel.get(projectStore);
    wLevel.setOptionsForWarningLevel(options);
    if (!ideMode) {
      if (launchRecord.formattingPrettyPrint.get(launchStore)) options.prettyPrint = true;
      if (launchRecord.formattingPrintInputDelimiter.get(launchStore)) options.printInputDelimiter = true;
    }

    options.closurePass = projectRecord.processClosurePrimitives.get(projectStore);

    options.jqueryPass = projectRecord.processJQueryPrimitives.get(projectStore) &&
        CompilationLevel.ADVANCED_OPTIMIZATIONS == level;

    if (projectRecord.processJQueryPrimitives.get(projectStore)) {
      options.setCodingConvention(new JqueryCodingConvention());
    }

    /*
    if (!flags.translationsFile.isEmpty()) {
      try {
        options.messageBundle = new XtbMessageBundle(
            new FileInputStream(flags.translationsFile),
            flags.translationsProject);
      } catch (IOException e) {
        throw new RuntimeException("Reading XTB file", e);
      }
    } else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
      // In SIMPLE or WHITESPACE mode, if the user hasn't specified a
      // translations file, they might reasonably try to write their own
      // implementation of goog.getMsg that makes the substitution at
      // run-time.
      //
      // In ADVANCED mode, goog.getMsg is going to be renamed anyway,
      // so we might as well inline it.
      options.messageBundle = new EmptyMessageBundle();
      
      // From AbstractCommandLineRunner.setRunOptions()
      if (config.warningGuards != null) {
        for (WarningGuardSpec.Entry entry : config.warningGuards.entries) {
          diagnosticGroups.setWarningLevel(options, entry.groupName, entry.level);
        }
      }
      */
    //createDefineOrTweakReplacements(config.define, options, false);

    //options.setTweakProcessing(config.tweakProcessing);
    //createDefineOrTweakReplacements(config.tweak, options, true);

    // Dependency options
    // options.setManageClosureDependencies(false);
    // if (config.closureEntryPoints.size() > 0) {
    //   options.setManageClosureDependencies(config.closureEntryPoints);
    // }

    options.ideMode = ideMode;
    // options.setCodingConvention(config.codingConvention);
    // options.setSummaryDetailLevel(config.summaryDetailLevel);

    // legacyOutputCharset = options.outputCharset = getLegacyOutputCharset();
    // outputCharset2 = getOutputCharset2();
    // inputCharset = getInputCharset();

    // if (config.createSourceMap.length() > 0) {
    //   options.sourceMapOutputPath = config.createSourceMap;
    // }
    // options.sourceMapDetailLevel = config.sourceMapDetailLevel;
    // options.sourceMapFormat = config.sourceMapFormat;

    // if (!config.variableMapInputFile.equals("")) {
    //   options.inputVariableMapSerialized =
    //       VariableMap.load(config.variableMapInputFile).toBytes();
    // }

    // if (!config.propertyMapInputFile.equals("")) {
    //   options.inputPropertyMapSerialized =
    //       VariableMap.load(config.propertyMapInputFile).toBytes();
    // }
    options.setLanguageIn(projectRecord.languageIn.get(projectStore));

    // if (!config.outputManifests.isEmpty()) {
    //   Set<String> uniqueNames = Sets.newHashSet();
    //   for (String filename : config.outputManifests) {
    //     if (!uniqueNames.add(filename)) {
    //       throw new FlagUsageException("output_manifest flags specify " +
    //           "duplicate file names: " + filename);
    //     }
    //   }
    // }

    // if (!config.outputBundles.isEmpty()) {
    //   Set<String> uniqueNames = Sets.newHashSet();
    //   for (String filename : config.outputBundles) {
    //     if (!uniqueNames.add(filename)) {
    //       throw new FlagUsageException("output_bundle flags specify " +
    //           "duplicate file names: " + filename);
    //     }
    //   }
    // }

    options.setAcceptConstKeyword(projectRecord.acceptConstKeyword.get(projectStore));
    // options.transformAMDToCJSModules = config.transformAMDToCJSModules;
    // options.processCommonJSModules = config.processCommonJSModules;
    // options.commonJSModulePathPrefix = config.commonJSModulePathPrefix;

    options.setRewriteNewDateGoogNow(false);
    options.setRemoveAbstractMethods(false);
    options.checkTypes = true;
    options.setInferTypes(true);
    options.closurePass = true;

    return options;
  }

  public static CompilerOptions makeForBuilder(IProject project) throws CoreException {
    return makeInternal(project, null, true);
  }
  
  public static CompilerOptions makeForLaunch(IProject project, ILaunchConfiguration config) throws CoreException {
    return makeInternal(project, new LaunchConfigurationReadOnlyStore(config), false);
  }

}
