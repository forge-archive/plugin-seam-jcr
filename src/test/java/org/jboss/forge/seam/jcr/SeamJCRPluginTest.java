package org.jboss.forge.seam.jcr;

import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.jboss.arquillian.api.Deployment;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

/**
 * {@link SeamJCRPlugin} test case
 * 
 * @author George Gastaldi (gastaldi)
 */
public class SeamJCRPluginTest extends AbstractShellTest
{

   @Test
   public void testNoParameters() throws IOException
   {
      Project project = initializeJavaProject();
      // Execute SUT
      getShell().execute("seam-jcr");
      DependencyFacet facet = project.getFacet(DependencyFacet.class);
      boolean actual = facet.hasDependency(DependencyBuilder.create("org.jboss.seam.jcr:seam-jcr"));
      assertFalse("Seam JCR dependency missing", actual);
   }

   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment().addPackages(true, SeamJCRPlugin.class.getPackage());
   }

}
