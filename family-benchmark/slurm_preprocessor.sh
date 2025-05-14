#!/bin/bash
#SBATCH --job-name=preprocessing
#SBATCH --time=48:00:00
#SBATCH -N 1
#SBATCH --ntasks-per-node=1
#SBATCH --partition=defq
#SBATCH --output=slurm_preprocessor.out
#SBATCH --nodelist=
status=$(grep 'summary_status' state.toml | cut -d'=' -f2 | tr -d ' "')
if [[ "$status" == "ready_to_start" ]]; then
  /usr/bin/time -v ../code/bin/preprocessor /home/patrickk/Gits/Tools/ontology-tools/family-benchmark_rich_background.owl.nt ./ --types_to_predicates
  if [ $? -eq 0 ]; then
    sed -i '/^summary_status =/c\summary_status = "preprocessed"' state.toml
  else
    sed -i '/^summary_status =/c\summary_status = "failed_to_preprocess"' state.toml
  fi
else
  warning_message="Did not preprocess, because the experiment is not in the right state. Expected state: \"ready_to_start\". Actual state: \"$status\"."
  echo $warning_message
  echo $(date) $(hostname) "Prepocessor.Warning: $warning_message" >> ../family-benchmark_rich_background.owl/experiments.log
fi
