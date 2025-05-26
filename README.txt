1. Compile the java part:

  - make sure to have maven installen
  - go into the bisimulation folder
  - execute "mvn package"
  - move bisimulation-graphs-1.0-SNAPSHOT-jar-with-dependencies.jar from the target subdirectory to the root folder

2. Compile the summarization component
  - create a folder lod_summarization/external
  - go into lod_summarization/setup an execute "./setup_experiments.sh -y"
  - node down the ID in the created folder as in "lod_summarization/d9a8fda5352cb6f8499e02d615b03d5a2831d51d"

3. Adaptations
  - replace the ID in line 7 in the computeHierarchy.sh script
  - in lod_summarization/ID/scripts/preprocessor.config, ensure that "types_to_predicates=true"

