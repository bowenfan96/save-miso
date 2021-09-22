import flask
import pandas as pd
import boto3
import geopy.distance

app = flask.Flask(__name__)

# AWS S3 variables
ACCESS_ID = "AKIAIOIGNRQI7AIRYWEA"
SECRET_KEY = "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"

s3 = boto3.client('s3',
                  aws_access_key_id=ACCESS_ID,
                  aws_secret_access_key=SECRET_KEY)


def filename():
    carbon_offset = "https://savemiso.s3-ap-southeast-1.amazonaws.com/carbon_offset.csv"
    user_info = "https://savemiso.s3-ap-southeast-1.amazonaws.com/user.csv"
    return carbon_offset, user_info


@app.route('/item/<item>')
def get_carbon_offset(item):
    """
    lookup the carbon offset amount based on input
    :param item:
    :return: str
    """
    carbon_offset, user_info = filename()
    df = pd.read_csv(carbon_offset, delimiter=r";")
    try:
        score_index = df[df['item'] == item].index[0]
    except IndexError:
        return "Item does not have a carbon offset value yet!"
    amount = df.iloc[score_index, -1]

    # Return rounded integer value in grams as a string
    return str(int(amount * 1000))


@app.route('/newuser/<username>')
def new_user(username):
    """
    add a new user
    :param username:
    :return: str
    """
    carbon_offset, user_info = filename()
    # check username does not contain invalid characters
    forbidden = [";", "#"]
    for char in username:
        if char in forbidden:
            return "Username contains invalid characters."

    # check no duplicate
    df = pd.read_table(user_info, delimiter=r";")
    if any(df.name == username):
        return "This username is taken"

    # update data in csv
    new_row = {'name': username, 'score': 0, 'health': 0}
    df = df.append(new_row, ignore_index=True)
    df.to_csv("user.csv", sep=";", index=False)

    # save csv
    with open("user.csv", "rb") as f:
        s3.upload_fileobj(f, "savemiso", "user.csv")
    return username + " is created as a new user with a score and health of 0."


@app.route('/delete/<username>')
def delete_user(username):
    """
    delete a username and relevant information in database
    :param username: valid username
    :return: confirmation of deletion or otherwise
    """
    carbon_offset, user_info = filename()
    df = pd.read_table(user_info, delimiter=r";")
    if any(df.name == username):
        df = df[df.name != username]
        df.to_csv("user.csv", sep=";", index=False)

        with open("user.csv", "rb") as f:
            s3.upload_fileobj(f, "savemiso", "user.csv")
        return "Deletion successful"
    else:
        return "Username cannot be found"


@app.route('/instructions/<item>')
def instructions(item_location):
    """
    lookup the recycling instructions based on the item and the user location
    :param item_location: semi-colon seperated <item;lat;long>
    :return: str What to do with the item and where
    """

    count = 0
    for char in item_location:
        if char == ";":
            count += 1

    if count != 2:
        return "Invalid format. Format must be &lt;item;lat;long> or &lt;item;;>."

    itemLocation = item_location.split(';')
    item = itemLocation[0]
    lat_usr = itemLocation[1]
    long_usr = itemLocation[2]

    # For electronics
    electronic_location = "https://savemiso.s3-ap-southeast-1.amazonaws.com/RENEW.csv"
    electronics_df = pd.read_table(electronic_location, delimiter=r",")
    electronic_instruction = "Please tape up loose ends of the batteries, or seal batteries in ziplock bags before depositing them into the RENEW bins."

    # Others
    others_instruction = "Please ensure that the recyclables do not contain food or liquid waste. Place recyclables in a household recycling receptacle and deposit recyclables into recycling bin."

    # non-recyclables
    non_instruction = "This item cannot be recycled."

    recyclables = ["Can", "Plastic Bottle"]
    if item in recyclables:
        return others_instruction

    electronics = ["Answering machines", "Cables", "Car stereos", "Computer Mice", "CDs/ DVDs", "Docking stations",
                   "DVD players", "Electronic Toys", "Hard drives", "Keyboards", "Laptops", "Lithium-ion (Li-on) batteries",
                   "Mobile phones", "Modems", "MP3 players", "Printed circuit boards", "Printers", "Remote controls",
                   "Routers", "Set-top Boxes", "Telephones", "VCD players", "VCRs", "Wires"]
    if item in electronics:
        # return instruction if no lat long is provided
        if lat_usr == "" or long_usr == "":
            return electronic_instruction

        # return both instruction and top 3 nearest location if lat long is provided
        electronics_df["distance"] = 0
        for row in range(0, electronics_df.shape[0]):
            lat_RENEW = electronics_df.iloc[row, 5]
            long_RENEW = electronics_df.iloc[row, 6]
            electronics_df.iloc[row, 7] = geopy.distance.distance((lat_usr, long_usr), (lat_RENEW, long_RENEW)).km

        # find top 3 nearest locations
        nearest_df = electronics_df.nsmallest(3, 'distance').to_dict(orient="index")
        nearest_df[99999] = electronic_instruction
        return nearest_df

    return non_instruction


@app.route('/scores/<username>')
def user_score(username):
    """
    lookup the user highscore
    :param username:
    :return: str
    """
    carbon_offset, user_info = filename()
    df = pd.read_table(user_info, delimiter=r";")
    try:
        score_index = df[df['name'] == username].index[0]
    except IndexError:
        return "User cannot be found in the database."
    score = df.iloc[score_index, 1]

    # Return integer value as a string
    return str(int(score))


@app.route('/health/<username>')
def user_health(username):
    """
    lookup the user highscore
    :param username:
    :return: str
    """
    carbon_offset, user_info = filename()
    df = pd.read_table(user_info, delimiter=r";")
    try:
        health_index = df[df['name'] == username].index[0]
    except IndexError:
        return "User cannot be found in the database."
    health = df.iloc[health_index, 2]

    # Return integer value as a string
    return str(int(health))


@app.route('/updatehealth/<userHealth>')
def change_health(userHealth):
    """
    update a user's Miso health in the format name;new_health
    :param userScore: semi-colon seperated <user;health>
    :return: str health of user's miso
    """
    carbon_offset, user_info = filename()
    df = pd.read_table(user_info, delimiter=r";")

    count = 0
    for char in userHealth:
        if char == ";":
            count += 1

    if count != 1:
        return "Invalid format. Format must be &lt;username;health>."

    try:
        change_list = userHealth.split(';')
        username = change_list[0]
        new_health = change_list[1]
        health_index = df[df['name'] == username].index[0]
    except IndexError:
        return "User cannot be found in the database."

    if int(new_health) < 0:
        return "Health cannot be less than 0."

    df.at[health_index, 'health'] = new_health
    df.to_csv("user.csv", sep=";", index=False)

    with open("user.csv", "rb") as f:
        s3.upload_fileobj(f, "savemiso", "user.csv")

    return username + "'s Miso has an updated health of " + str(new_health) + "!!"


@app.route('/update/<userScore>')
def change_score(userScore):
    """
    update a user's score in the format name;new_score
    :param userScore: semi-colon seperated <user;score>
    :return: str score of user
    """
    carbon_offset, user_info = filename()
    df = pd.read_table(user_info, delimiter=r";")

    count = 0
    for char in userScore:
        if char == ";":
            count += 1

    if count != 1:
        return "Invalid format. Format must be &lt;username;score>."

    try:
        change_list = userScore.split(';')
        username = change_list[0]
        new_score = change_list[1]
        score_index = df[df['name'] == username].index[0]
    except IndexError:
        return "User cannot be found in the database."

    if int(new_score) < 0:
        return "Score cannot be less than 0."

    df.at[score_index, 'score'] = new_score
    df.to_csv("user.csv", sep=";", index=False)

    with open("user.csv", "rb") as f:
        s3.upload_fileobj(f, "savemiso", "user.csv")

    return username + " has an updated score of " + str(new_score) + "!!"


@app.route('/')
def index():
    return "/item, /newuser, /scores, /delete, /update, /instructions, /health, /updatehealth"


if __name__ == '__main__':  # pragma: no branch
    app.run()

#############################################################
############### WE ARE NOT USING POST METHODS ###############
#############################################################


# @app.route('/api/post_some_data', methods=['POST'])
# def get_text_prediction():
#     """
#     predicts requested text whether it is ham or spam
#     :return: json
#     """
#     json = flask.request.get_json()
#     print(json)
#     if len(json['text']) == 0:
#         return flask.jsonify({'error': 'invalid input'})
#
#     return flask.jsonify({'you sent this': json['text']})
#
#
# @app.route('/api', methods=['POST'])
# def handle_request():
#     imagefile = flask.request.files['image']
#     filename = werkzeug.utils.secure_filename(imagefile.filename)
#     print("\nReceived image File name : " + imagefile.filename)
#     imagefile.save(filename)
#     return "Image Uploaded Successfully"
