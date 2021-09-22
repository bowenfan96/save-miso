import unittest
from app import *

# coverage run -m unittest discover <<- run this first to discover all new tests
# coverage report app.py
# coverage html app.py
# coverage run --branch -m unittest discover
# coverage xml

'''
BASE_URL = "http://127.0.0.1:5000/"
def get_offset(path):
    joined_url = urljoin(BASE_URL, path)
    response = requests.get(joined_url)
    print(response.url)
    if response.ok:
        return response
    else:
        return None
'''


class TestCarbonOffset(unittest.TestCase):
    def test1_getting_carbon_offset(self):
        item = "Can"
        response = get_carbon_offset(item)
        self.assertEqual(response, "120", msg="Failed to retrieve Carbon Offset")

    def test2_none_existent_item(self):
        item = "None"
        expected = "Item does not have a carbon offset value yet!"
        response = get_carbon_offset(item)
        self.assertEqual(response, expected, msg="Failed to spot none existent item")


class TestInstructions(unittest.TestCase):
    def test1_getting_electronics_instructions(self):
        item_location = "VCRs;1.33255;103.92"
        response = instructions(item_location)
        expected = {46: {'Access': 'Public',
      'Address': '180 Bedok Reservoir Road',
      'Building': 'Eunos CC',
      'Details': 'Level 1, near CC Office',
      'Postal': 479220,
      'distance': 0.5404727128785756,
      'latitude': 1.33255278513217,
      'longitude': 103.915143547205},
 68: {'Access': 'Public',
      'Address': '5 Lengkong Tiga',
      'Building': 'Kampong Kembangan CC',
      'Details': 'CC Foyer',
      'Postal': 417408,
      'distance': 1.3188413493029523,
      'latitude': 1.32313367852174,
      'longitude': 103.912726547104},
 133: {'Access': 'Public',
       'Address': '750 Chai Chee Road',
       'Building': 'Viva Business Park',
       'Details': '750A at back entrance of Decathlon, next to PopStation',
       'Postal': 469000,
       'distance': 0.9959078678511378,
       'latitude': 1.32384714537839,
       'longitude': 103.922304551155},
 99999: 'Please tape up loose ends of the batteries, or seal batteries in '
        'ziplock bags before depositing them into the RENEW bins.'}
        self.assertEqual(response, expected, msg="Failed to retrieve electronics instructions")

    def test_non_recyclables(self):
        item = "clock;;"
        expected = "This item cannot be recycled."
        response = instructions(item)
        self.assertEqual(response, expected, msg="Failed to retrieve non-recyclable instructions")

    def test_recyclable_instruction(self):
        item = "Can;;"
        expected = "Please ensure that the recyclables do not contain food or liquid waste. Place recyclables in a household recycling receptacle and deposit recyclables into recycling bin."
        response = instructions(item)
        self.assertEqual(response, expected, msg="Failed to retrieve non-recyclable instructions")

    def testInvalidFormat(self):
        item = "Can"
        expected = "Invalid format. Format must be &lt;item;lat;long> or &lt;item;;>."
        response = instructions(item)
        self.assertEqual(response, expected, msg="Failed to detect invalid instructions")

class TestUsers(unittest.TestCase):
    def test1_add_users(self):
        username = 'user1'
        expected = username + " is created as a new user with a score and health of 0."
        response = new_user(username)
        self.assertEqual(response, expected, msg="Failed to add new users")

    def test2_no_duplicate_users(self):
        username = 'user1'
        expected = "This username is taken"
        response = new_user(username)
        self.assertEqual(response, expected, msg="Failed to detect duplicate users")

    def test3_delete_user(self):
        username = 'user1'
        expected = "Deletion successful"
        response = delete_user(username)
        self.assertEqual(response, expected, msg="Failed to delete user")
        response = delete_user(username)
        expected = "Username cannot be found"
        self.assertEqual(response, expected, msg="Failed to detect non-existent user")

    def test4_no_forbidden_char(self):
        username = 'user1;'
        expected = "Username contains invalid characters."
        response = new_user(username)
        self.assertEqual(response, expected, msg="Failed to detect invalid character")


class TestGettingScore(unittest.TestCase):
    def test1_get_score(self):
        username = 'user2'
        score = 0
        new_user(username)
        response = user_score(username)
        self.assertEqual(response, "0", msg="Failed to retrieve score")

    def test2_change_score(self):
        username = 'user2'
        score = 1000
        userscore = username + ";" + str(score)
        expected = username + " has an updated score of " + str(score) + "!!"
        response = change_score(userscore)
        self.assertEqual(response, expected, msg="Failed to update score")

    def test3_change_negative_score(self):
        username = 'user2'
        score = -1000
        userscore = username + ";" + str(score)
        expected = "Score cannot be less than 0."
        response = change_score(userscore)
        self.assertEqual(response, expected, msg="Failed to prevent negative score")

    def test4_invalid_score_format(self):
        username = 'user2'
        score = 10
        userscore = username + "#" + str(score)
        expected = "Invalid format. Format must be &lt;username;score>."
        response = change_score(userscore)
        self.assertEqual(response, expected, msg="Failed to reject invalid score format")
        delete_user(username)

    def test5_cannot_find_user(self):
        username = 'nonexistent'
        score = 10
        userscore = username + ";" + str(score)
        expected = "User cannot be found in the database."
        response = change_score(userscore)
        self.assertEqual(response, expected, msg="Failed to detect non-existent user")
        delete_user(username)


class TestGettingHealth(unittest.TestCase):
    def test1_get_health(self):
        username = 'user2'
        new_user(username)
        response = user_health(username)
        self.assertEqual(response, "0", msg="Failed to retrieve health")

    def test2_change_health(self):
        username = 'user2'
        health = 1000
        userscore = username + ";" + str(health)
        expected = username + "'s Miso has an updated health of " + str(health) + "!!"
        response = change_health(userscore)
        self.assertEqual(response, expected, msg="Failed to update health")

    def test3_change_negative_health(self):
        username = 'user2'
        health = -1000
        userhealth = username + ";" + str(health)
        expected = "Health cannot be less than 0."
        response = change_health(userhealth)
        self.assertEqual(response, expected, msg="Failed to prevent negative health")

    def test4_invalid_score_format(self):
        username = 'user2'
        health = 10
        userhealth = username + "#" + str(health)
        expected = "Invalid format. Format must be &lt;username;score>."
        response = change_score(userhealth)
        self.assertEqual(response, expected, msg="Failed to reject invalid health format")
        delete_user(username)

    def test5_cannot_find_user(self):
        username = 'nonexistent'
        health = 10
        userhealth = username + ";" + str(health)
        expected = "User cannot be found in the database."
        response = change_health(userhealth)
        self.assertEqual(response, expected, msg="Failed to detect non-existent user")
        delete_user(username)