package org.jboss.forge.seam.jcr;

import java.util.EnumSet;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.Dependency;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;

/**
 * Seam JCR Forge Plugin
 * 
 * @author George Gastaldi (gastaldi)
 * 
 */
@Alias("seam-jcr")
@RequiresFacet(DependencyFacet.class)
public class SeamJCRPlugin implements Plugin
{

   @Inject
   Project project;
   @Inject
   ShellPrompt prompt;

   @DefaultCommand
   public void help(PipeOut out)
   {
      out.println("The following parameters are available:");
      out.println(" setup --provider " + EnumSet.allOf(Provider.class));
   }

   @Command(value = "setup")
   public void setup(@Option(name = "provider") Provider provider)
   {
      DependencyFacet dependencyFacet = project.getFacet(DependencyFacet.class);
      DependencyBuilder seamjcrDependency = DependencyBuilder.create().setGroupId("org.jboss.seam.jcr")
                .setArtifactId("seam-jcr");

      if (!dependencyFacet.hasDirectDependency(seamjcrDependency))
      {
         if (!dependencyFacet.hasRepository(DependencyFacet.KnownRepository.JBOSS_NEXUS))
         {
            dependencyFacet.addRepository(DependencyFacet.KnownRepository.JBOSS_NEXUS);
         }

         List<Dependency> versions = dependencyFacet.resolveAvailableVersions(seamjcrDependency);

         Dependency choosenVersion = prompt.promptChoiceTyped("Which version of Seam JCR do you want to install?",
                    versions, versions.get(versions.size() - 1));
         dependencyFacet.setProperty("seam.jcr.version", choosenVersion.getVersion());

         dependencyFacet.addDirectDependency(seamjcrDependency.setVersion("${seam.jcr.version}"));
      }
      if (provider != null)
      {
          //TODO: Add Modeshape dependencies or Jackrabbit dependencies ?
      }
   }

   public static enum Provider
   {
      JACKRABBIT, MODESHAPE;
   }
}
