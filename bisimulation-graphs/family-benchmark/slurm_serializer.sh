#!/bin/bash
#SBATCH --job-name=serializing_to_ntriples
#SBATCH --time=48:00:00
#SBATCH -N 1
#SBATCH --ntasks-per-node=1
#SBATCH --partition=defq
#SBATCH --output=slurm_serializer.out
#SBATCH --nodelist=
working_directory=$(pwd)
source activate base
source $HOME/.bashrc
cd $working_directory  # We have to move back to the working directory, as .bashrc might contain code to change the directory
conda activate
conda activate $working_directory/../code/python/.conda/

status=$(grep 'summary_status' state.toml | cut -d'=' -f2 | tr -d ' "')
if [[ "$status" == "multi_summary_complete" ]]; then
  /usr/bin/time -v python $working_directory/../code/python/summary_loader/serialize_to_ntriples.py ./ hash
  if [ $? -eq 0 ]; then
    sed -i '/^serialized =/c\serialized = true' state.toml
  else
    error_message="Serializing did not finish with exit status 0."
    echo $error_message
    echo $(date) $(hostname) "Serializer.Err: $error_message" >> ../family-benchmark_rich_background.owl/experiments.log
  fi
else
  warning_message="Did not serialize, because the experiment is not in the right state. Expected state: \"multi_summary_complete\". Actual state: \"$status\"."
  echo $warning_message
  echo $(date) $(hostname) "Serializer.Warning: $warning_message" >> ../family-benchmark_rich_background.owl/experiments.log
fi
