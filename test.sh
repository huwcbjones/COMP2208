#!/bin/bash
fFlag=false;
tFlag=false;
output=".";
while getopts 't:f:o:' flag; do
  case "${flag}" in
    o) output="${OPTARG}" ;;
    t) type="${OPTARG}" ; tFlag=true ;;
    f) file="${OPTARG}" ; fFlag=true ;;
    *) echo "Unexpected option ${flag}" ; exit ;
  esac
done

if ! $fFlag
then
  echo "File of states must be provided with -f!";
  exit 1
fi

if ! $tFlag
then
  echo "Search type must be provided with -t!";
  exit 1
fi

cat "$file" | awk '
BEGIN{
  RS = "\n";
  FS = ",";
}
{
  output="'$output'/'$type'-"$1".txt";
  split($2, parts, ":");
  height=parts[2];
  width=parts[1];
  if(NF == 2){
    system("./run.sh -w \""width"\" -h \""height"\" -t \"'$type'\" -s \""$2"\" > "output);
  } else {
    system("./run.sh -w "width" -h "height" -t \"'$type'\" -s \""$2"\" -e \""$3"\" > "output);
  }
}
'
