package ${package};

import org.junit.runner.RunWith;

public class ${artifactId}Test extends JUnitStory {

    public LinkedList<Object> stepDefinitions = new LinkedList<Object>();

    @Override
    public Configuration configuration() {
     return new MostUsefulConfiguration()
       .useStoryLoader(new LoadFromClasspath(this.getClass()))
       .useStoryReporterBuilder(
         new StoryReporterBuilder()
           .withRelativeDirectory("")
           .withDefaultFormats()
           .withFormats(Format.CONSOLE, Format.TXT,
             Format.XML, Format.HTML));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
     return new InstanceStepsFactory(configuration(), this.stepDefinitions);
    }

    public ${artifactId}Test() {
     this.stepDefinitions.add(new ${artifactId}Steps());
    }
   }