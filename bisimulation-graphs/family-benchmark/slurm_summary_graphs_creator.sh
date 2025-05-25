#!/bin/bash
#SBATCH --job-name=summary_graphs_creation
#SBATCH --time=01:00:00
#SBATCH -N 1
#SBATCH --ntasks-per-node=1
#SBATCH --partition=defq
#SBATCH --output=slurm_summary_graphs_creator.out
#SBATCH --nodelist=
status=$(grep 'summary_status' state.toml | cut -d'=' -f2 | tr -d ' "')
if [[ "$status" == "bisimulation_complete" ]]; then
  /usr/bin/time -v ../code/bin/create_condensed_summary_graph_from_partitions ./
  if [ $? -eq 0 ]; then
    sed -i '/^summary_status =/c\summary_status = "multi_summary_complete"' state.toml
  else
    sed -i '/^summary_status =/c\summary_status = "failed_to_create_multi_summary"' state.toml
  fi
else
  warning_message="Did not create the multi summary, because the experiment is not in the right state. Expected state: \"bisimulation_complete\". Actual state: \"$status\"."
  echo $warning_message
  echo $(date) $(hostname) "Summary_Graphs_Creator.Warning: $warning_message" >> ../family-benchmark_rich_background.owl/experiments.log
fi
