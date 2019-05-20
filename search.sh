#!/bin/sh


################################################
#                                              #
# Script to display Search Options for Zendesk #
#                                              #
################################################

JOB_HOME_DIR=/Users/archanask/Documents/ArchanaPersonal/Zendesk/CodingChallenge
LOG_DIR=${JOB_HOME_DIR}/logs
SPARK_HOME_DIR=${JOB_HOME_DIR}/spark-2.4.3-bin-hadoop2.7
SCALA_JAR_LOC=${JOB_HOME_DIR}
OUTPUT_FILE_PATH=${JOB_HOME_DIR}/file.out

echo "*****************************************************************"
echo "                   Welcome To Zendesk Search                     "
echo "*****************************************************************"
printf  "\n\n\n\n\n\n"


while :
do
dt=`date "+%Y%m%d%H%M%S"`
PS3='Please enter your choice: '
options=("Option 1: Search" "Option 2: View List")
selection=0
searchSelection=("Users" "Tickets" "Organizations")
select opt in "${options[@]}"
do
    case $opt in
        "Option 1: Search")
            echo "you chose choice 1"
            selection=1
            break;
            ;;
        "Option 2: View List")
            echo "you chose choice 2"
            selection=2
            break;
            ;;
        *) echo "invalid option $REPLY";;
    esac
done


if [ $selection -eq 1 ]; then 
  echo "Select your search option"
  echo "1. Users   2. Tickets  3. Organizations"
  read -p "Enter Option: " subOption
  if [ \( $subOption -eq 1 \) -o \( $subOption -eq 2 \) -o \( $subOption -eq 3 \) ]; then
    printf "Enter Search Term: " 
    read searchTerm
    printf "Enter Search Value: "
    read searchValue
    echo "**************"
    if [ $subOption -eq 1 ]; then
      optn=Users
    elif [ $subOption -eq 2 ]; then
      optn=Tickets
    elif [ $subOption -eq 3 ]; then
      optn=Organizations
    fi

    echo "Executing Spark Code with args as Option ${optn}, searchTerm ${searchTerm} and searchValue ${searchValue}"
   # Invoke Spark Code
 ${SPARK_HOME_DIR}/bin/spark-submit --files config.properties ${SCALA_JAR_LOC}/jsondatasearch_2.11-0.1.jar $optn $searchTerm $searchValue >> ${LOG_DIR}/dataSearch_${dt}.log 2>&1

retVal=$?
if [ $retVal -ne 0 ]; then

  printf  "\n\n\n"
  echo "       Job Search encountered error. Please visit ${LOG_DIR} for more information..."
else
    # Displaying output file
    cat $OUTPUT_FILE_PATH
fi
  
  else
    echo "Invalid Selection"
  fi
elif [ $selection -eq 2 ]; then
  echo "Search Users With\n"
  printf "_id\nurl\nexternal_id\nname\nalias\ncreated_at\nactive\nverified\nshared\nlocale\ntimezone\nlast_login_at\nemail\nphone\nsignature\norganization_id\ntags\nsuspended\nrole"
  printf "\n\n\n--------------------------------------------------------"
  printf "\nSearch Tickets With\n"
  printf "_id\nurl\nexternal_id\ncreated_at\ntype\nsubject\ndescription\npriority\nstatus\nrecipient\nsubmitter_id\nassignee_id\norganization_id\ntags\nhas_incidents\ndue_at\nvia\nrequestor_id"
  printf "\n\n\n--------------------------------------------------------"
  printf "\nSearch Organizations With\n"
  printf "_id\nurl\nexternal_id\nname\ndomain_names\ncreated_at\ndetails\nshared_tickets\ntags"
  printf "\n\n\n--------------------------------------------------------"

fi

echo "Press [CTRL+C] to stop.."
sleep 1

done
