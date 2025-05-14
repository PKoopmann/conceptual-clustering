#!/bin/bash
#SBATCH --job-name=bisimulating
#SBATCH --time=48:00:00
#SBATCH -N 1
#SBATCH --ntasks-per-node=1
#SBATCH --partition=defq
#SBATCH --output=slurm_bisimulator.out
#SBATCH --nodelist=
status=$(grep 'summary_status' state.toml | cut -d'=' -f2 | tr -d ' "')
if [[ "$status" == "preprocessed" ]]; then
  /usr/bin/time -v ../code/bin/bisimulator run_k_bisimulation_store_partition_condensed_timed ./binary_encoding.bin --output=./ --typed_start
  if [ $? -eq 0 ]; then
    sed -i '/^summary_status =/c\summary_status = "bisimulation_complete"' state.toml
  else
    sed -i '/^summary_status =/c\summary_status = "failed_to_bisimulate"' state.toml
  fi
else
  warning_message="Did not bisimulate, because the experiment is not in the right state. Expected state: \"preprocessed\". Actual state: \"$status\"."
  echo $warning_message
  echo $(date) $(hostname) "Bisimulator.Warning: $warning_message" >> ../family-benchmark_rich_background.owl/experiments.log
fi
