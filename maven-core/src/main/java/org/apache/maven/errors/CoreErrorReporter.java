package org.apache.maven.errors;

import org.apache.maven.NoGoalsSpecifiedException;
import org.apache.maven.ProjectCycleException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataRetrievalException;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.execution.RealmManagementException;
import org.apache.maven.extension.ExtensionManagerException;
import org.apache.maven.lifecycle.LifecycleException;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.lifecycle.LifecycleLoaderException;
import org.apache.maven.lifecycle.LifecycleSpecificationException;
import org.apache.maven.lifecycle.TaskValidationResult;
import org.apache.maven.lifecycle.model.MojoBinding;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginConfigurationException;
import org.apache.maven.plugin.PluginExecutionException;
import org.apache.maven.plugin.PluginManagerException;
import org.apache.maven.plugin.PluginNotFoundException;
import org.apache.maven.plugin.PluginParameterException;
import org.apache.maven.plugin.descriptor.Parameter;
import org.apache.maven.plugin.loader.PluginLoaderException;
import org.apache.maven.plugin.version.PluginVersionNotFoundException;
import org.apache.maven.plugin.version.PluginVersionResolutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;
import org.apache.maven.project.error.ProjectErrorReporter;
import org.apache.maven.project.interpolation.ModelInterpolationException;
import org.apache.maven.project.path.PathTranslator;
import org.apache.maven.reactor.MavenExecutionException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CoreErrorReporter
    extends ProjectErrorReporter
{

    void handleSuperPomBuildingError( ProjectBuildingException exception );

    void reportAggregatedMojoFailureException( MavenSession session, MojoBinding binding, MojoFailureException cause );

    void reportAttemptToOverrideUneditableMojoParameter( Parameter currentParameter, MojoBinding binding, MavenProject project, MavenSession session, MojoExecution exec, PathTranslator translator, Logger logger, PluginConfigurationException cause );

    void reportErrorApplyingMojoConfiguration( MojoBinding binding, MavenProject project, PlexusConfiguration config, PluginConfigurationException cause );

    void reportErrorConfiguringExtensionPluginRealm( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, PluginManagerException cause );

    void reportErrorFormulatingBuildPlan( List tasks, MavenProject configuringProject, String targetDescription, LifecycleException cause );

    void reportErrorInterpolatingModel( Model model, Map inheritedValues, File pomFile, MavenExecutionRequest request, ModelInterpolationException cause );

    void reportErrorLoadingPlugin( MojoBinding binding, MavenProject project, PluginLoaderException cause );

    void reportErrorManagingRealmForExtension( Artifact extensionArtifact, Artifact projectArtifact, List remoteRepos, MavenExecutionRequest request, RealmManagementException cause );

    void reportErrorManagingRealmForExtensionPlugin( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, RealmManagementException cause );

    void reportErrorResolvingExtensionDependencies( Artifact extensionArtifact, Artifact projectArtifact, List remoteRepos, MavenExecutionRequest request, ArtifactResolutionResult resolutionResult, ExtensionManagerException err );

    void reportErrorResolvingExtensionDirectDependencies( Artifact extensionArtifact, Artifact projectArtifact, List remoteRepos, MavenExecutionRequest request, ArtifactMetadataRetrievalException cause );

    void reportErrorSearchingforCompatibleExtensionPluginVersion( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, String requiredMavenVersion, String currentMavenVersion, InvalidVersionSpecificationException cause );

    void reportExtensionPluginArtifactNotFound( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, PluginNotFoundException cause );

    void reportExtensionPluginVersionNotFound( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, PluginVersionNotFoundException cause );

    void reportIncompatibleMavenVersionForExtensionPlugin( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, String requiredMavenVersion, String currentMavenVersion, PluginVersionResolutionException err );

    void reportInvalidDependencyVersionInExtensionPluginPOM( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, InvalidDependencyVersionException cause );

    void reportInvalidMavenVersion( MavenProject project, ArtifactVersion mavenVersion, MavenExecutionException err );

    void reportInvalidPluginExecutionEnvironment( MojoBinding binding, MavenProject project, PluginExecutionException cause );

    void reportLifecycleLoaderErrorWhileValidatingTask( MavenSession session, MavenProject rootProject, LifecycleLoaderException cause, TaskValidationResult result );

    void reportLifecycleSpecErrorWhileValidatingTask( MavenSession session, MavenProject rootProject, LifecycleSpecificationException cause, TaskValidationResult result );

    void reportMissingArtifactWhileAddingExtensionPlugin( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, ArtifactNotFoundException cause );

    void reportMissingPluginDescriptor( MojoBinding binding, MavenProject project, LifecycleExecutionException err );

    void reportMissingRequiredMojoParameter( MojoBinding binding, MavenProject project, List invalidParameters, PluginParameterException err );

    void reportMojoExecutionException( MojoBinding binding, MavenProject project, MojoExecutionException cause );

    void reportMojoLookupError( MojoBinding binding, MavenProject project, ComponentLookupException cause );

    void reportNoGoalsSpecifiedException( MavenProject rootProject, NoGoalsSpecifiedException error );

    void reportPluginErrorWhileValidatingTask( MavenSession session, MavenProject rootProject, PluginLoaderException cause, TaskValidationResult result );

    void reportPomFileCanonicalizationError( File pomFile, IOException cause );

    void reportPomFileScanningError( File basedir, String includes, String excludes, IOException cause );

    void reportProjectCycle( ProjectCycleException error );

    void reportProjectDependenciesNotFound( MavenProject project, String scope, ArtifactNotFoundException cause );

    void reportProjectDependenciesUnresolvable( MavenProject project, String scope, ArtifactResolutionException cause );

    void reportProjectDependencyArtifactNotFound( MavenProject project, Artifact artifact, ArtifactNotFoundException cause );

    void reportProjectDependencyArtifactUnresolvable( MavenProject project, Artifact artifact, ArtifactResolutionException cause );

    void reportProjectMojoFailureException( MavenSession session, MojoBinding binding, MojoFailureException cause );

    void reportReflectionErrorWhileEvaluatingMojoParameter( Parameter currentParameter, MojoBinding binding, MavenProject project, String expression, Exception cause );

    void reportUnresolvableArtifactWhileAddingExtensionPlugin( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, ArtifactResolutionException cause );

    void reportUnresolvableExtensionPluginPOM( Plugin plugin, Model originModel, List remoteRepos, MavenExecutionRequest request, ArtifactMetadataRetrievalException cause );

    void reportUseOfBannedMojoParameter( Parameter currentParameter, MojoBinding binding, MavenProject project, String expression, String altExpression, ExpressionEvaluationException err );

}